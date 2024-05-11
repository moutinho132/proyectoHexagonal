package com.martzatech.vdhg.crmprojectback.application.services;

import com.martzatech.vdhg.crmprojectback.application.enums.CustomerStatusEnum;
import com.martzatech.vdhg.crmprojectback.application.enums.DeletedStatusEnum;
import com.martzatech.vdhg.crmprojectback.application.exceptions.BusinessRuleException;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.File;
import com.martzatech.vdhg.crmprojectback.domains.chat.services.FileService;
import com.martzatech.vdhg.crmprojectback.domains.customers.enums.NoteTypeEnum;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Customer;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.CustomerStatus;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Note;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.NoteService;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.ProductService;
import com.martzatech.vdhg.crmprojectback.domains.security.models.DeletedStatus;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import com.martzatech.vdhg.crmprojectback.domains.vendors.models.Vendor;
import com.martzatech.vdhg.crmprojectback.domains.vendors.models.VendorContact;
import com.martzatech.vdhg.crmprojectback.domains.vendors.models.VendorLocation;
import com.martzatech.vdhg.crmprojectback.domains.vendors.services.VendorContactService;
import com.martzatech.vdhg.crmprojectback.domains.vendors.services.VendorLocationService;
import com.martzatech.vdhg.crmprojectback.domains.vendors.services.VendorService;
import com.martzatech.vdhg.crmprojectback.domains.vendors.specifications.VendorSpecification;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants.AZURE_PATH;
import static com.martzatech.vdhg.crmprojectback.application.helper.FileHelper.validateExtensionFile;

@AllArgsConstructor
@Slf4j
@Service
public class VendorManagementService {
    private static final String CREATION_PERMISSION = "VENDORS_CREATE_ALL";
    private static final String INTERNAL_PERMISSION = "VENDORS_VIEW_ALL";
    private static final String UPDATE_PERMISSION = "VENDORS_EDIT_ALL";
    private static final String DELETE_PERMISSION = "VENDORS_DELETE_ALL";
    private final VendorService vendorService;
    private final VendorLocationService locationService;
    private final VendorContactService contactService;
    private final SecurityManagementService securityManagementService;
    private final NoteService noteService;
    private final FileService fileService;
    private final AzureFileService azureFileService;
    private final ProductService productService;
    private static VendorContact getBuildUpdateVendor(VendorContact vendorContact, VendorContact contact) {
        return VendorContact
                .builder()
                .role(vendorContact.getRole())
                .additional(vendorContact.getAdditional())
                .telephone(vendorContact.getTelephone())
                .email(vendorContact.getEmail())
                .additional(vendorContact.getAdditional())
                .name(vendorContact.getName())
                .id(contact.getId())
                .build();
    }

    public Note saveNote(final Integer id, final Note note) {
        vendorService.existsById(id);

        final User currentUser = securityManagementService.findCurrentUser();
        final Note toSave = Objects.nonNull(note.getId())
                ? getPersistedNote(id, note, currentUser)
                : note.withType(NoteTypeEnum.VENDOR).withElementId(id);

        return noteService.save(
                toSave
                        .withCreationTime(
                                Objects.nonNull(note.getId()) ? toSave.getCreationTime() : LocalDateTime.now()
                        )
                        .withCreationUser(
                                Objects.nonNull(note.getId()) ? toSave.getCreationUser() : currentUser
                        )
                        .withStatus(Objects.nonNull(note.getId()) ? toSave.getStatus() : DeletedStatus.builder().id(1).build())
                        .withModificationTime(
                                Objects.nonNull(note.getId()) ? LocalDateTime.now() : null
                        )
                        .withModificationUser(
                                Objects.nonNull(note.getId()) ? currentUser : null
                        )
        );
    }

    private Note getPersistedNote(final Integer id, final Note note, final User currentUser) {
        final Note byId = noteService.findById(note.getId());

        if (!(noteService.canUpdateAll(currentUser)
                || (byId.getCreationUser().getId().intValue() == currentUser.getId()
                && noteService.canUpdateOwn(currentUser)))) {
            throw new BusinessRuleException(
                    String.format("Current user with id %s, cannot update this note.", currentUser.getId())
            );
        }

        if (byId.getType() != NoteTypeEnum.VENDOR) {
            throw new BusinessRuleException(
                    String.format("Current note type is %s. Note type cannot be changed.", byId.getType())
            );
        }

        if (byId.getElementId().intValue() != id) {
            throw new BusinessRuleException(
                    String.format("Current elementId is %s. ElementId cannot be changed.", byId.getElementId())
            );
        }

        return byId
                .withTitle(note.getTitle())
                .withDescription(note.getDescription())
                .withUsers(note.getUsers())
                .withRoles(note.getRoles());
    }
    @Transactional
    public List<Vendor> findAll(final VendorSpecification specification,
                                  final Integer page, final Integer size, final String direction, final String attribute) {
        hasAccessToVendor(securityManagementService.findCurrentUser());
        final Sort.Direction directionEnum =
                Arrays.stream(Sort.Direction.values()).anyMatch(v -> v.name().equalsIgnoreCase(direction))
                        ? Sort.Direction.fromString(direction)
                        : Sort.Direction.DESC;
        final Sort sort = Sort.by(directionEnum, attribute);
        final Pageable pageable = PageRequest.of(page, size, sort);
        return vendorService.findAll(specification, pageable).stream()
                .map(vendor -> vendor.withProducts(productService.findByVendor(vendor))
                        .withFiles(fileService.getFileVendor(vendor.getId())))
                .collect(Collectors.toList());
    }
    public Vendor findById(final Integer id){
        hasAccessToVendor(securityManagementService.findCurrentUser());
        Vendor vendor = vendorService.findById(id);
        return  vendor.withProducts(productService.findByVendor(vendor))
                .withFiles(fileService.getFileVendor(id));
    }
    private void hasAccessToVendor(User currentUser) {
        if (!isInternalUser(currentUser)) {
            throw new BusinessRuleException("Cannot View not a vendor");
        }
    }

    private void hasAccessDeleteToVendor(User currentUser) {
        if (!hasDeletePermission(currentUser)) {
            throw new BusinessRuleException("Cannot View not a vendor");
        }
    }
    private static boolean isInternalUser(final User currentUser) {
        return currentUser
                .getRoles()
                .stream()
                .flatMap(r -> r.getPermissions().stream())
                .anyMatch(p -> INTERNAL_PERMISSION.equalsIgnoreCase(p.getName()));
    }

    private static boolean hasDeletePermission(final User currentUser) {
        return currentUser
                .getRoles()
                .stream()
                .flatMap(r -> r.getPermissions().stream())
                .anyMatch(p -> DELETE_PERMISSION.equalsIgnoreCase(p.getName()));
    }
    private static boolean hasCreationPermission(final User currentUser) {
        return currentUser
                .getRoles()
                .stream()
                .flatMap(r -> r.getPermissions().stream())
                .anyMatch(p -> CREATION_PERMISSION.equalsIgnoreCase(p.getName()));
    }
    private static boolean hasUpdatePermission(final User currentUser) {
        return currentUser
                .getRoles()
                .stream()
                .flatMap(r -> r.getPermissions().stream())
                .anyMatch(p -> UPDATE_PERMISSION.equalsIgnoreCase(p.getName()));
    }
    /*
     * The core services
     */
    @Transactional
    public Vendor saveVendor(final Vendor model, final User user) {
        validations(model, user);
        return vendorService.save(build(model));
    }

    public File addFile(final Integer id, final MultipartFile file, final String extension) {
        String extensionFile = FilenameUtils.getExtension(file.getOriginalFilename());
        validateExtensionFile(extension, extensionFile);
        validId(id);
        Vendor vendor = vendorService.findById(id);
        File messageFile = null;
        if (Objects.nonNull(file)) {
            String urlFile = azureFileService.uploadFile(file, AZURE_PATH, extension);
            messageFile = fileService.save(
                    File.builder()
                            .url(urlFile)
                            .extension(extension)
                            .name(extractFileNameWithoutExtension(file.getOriginalFilename()))
                            .vendors(Objects.nonNull(vendor) ? List.of(vendor) : null)
                            .status(DeletedStatus.builder().id(DeletedStatusEnum.ACTIVE.getId()).build())
                            .creationUser( securityManagementService.findCurrentUser())
                            .creationTime(LocalDateTime.now())
                            .build()
            );
        }
        return messageFile;
    }

    public static String extractFileNameWithoutExtension(String fileName) {
        // Buscar el último punto (.) en la cadena
        int lastDotIndex = fileName.lastIndexOf('.');

        // Verificar si se encontró un punto
        if (lastDotIndex >= 0) {
            // Extraer el nombre del archivo sin la extensión
            return fileName.substring(0, lastDotIndex);
        } else {
            // Si no se encontró un punto, devolver la cadena original
            return fileName;
        }
    }
    private void validations(Vendor model, User user) {
        validations(model);
        validatePermissions(user);
    }
    private void validatePermissions(final User currentUser) {
        if (!hasCreationPermission(currentUser)) {
            throw new BusinessRuleException("Cannot create a vendor");
        }

        if (!hasUpdatePermission(currentUser)) {
            throw new BusinessRuleException("Cannot update any chat");
        }
    }
    public void deleteById(final Integer id) {
        hasAccessDeleteToVendor(securityManagementService.findCurrentUser());
        vendorService.deleteById(id);
    }
    /*
     * Validators
     */
    private void validations(final Vendor model) {
        validId(model.getId());
        validateContact(model);
        validateLocation(model);
        validateExistName(model);
    }
    private void validateLocation(Vendor model) {
        if (CollectionUtils.isEmpty(model.getVendorLocations())) {
            throw new BusinessRuleException("Location List is mandatory");
        }
    }
    private void validateContact(Vendor model) {
        if (CollectionUtils.isEmpty(model.getVendorContacts())) {
            throw new BusinessRuleException("Contact List is mandatory");
        }
    }
    private void validateExistName(Vendor model) {
        if (Objects.isNull(model.getId())) {
            if (StringUtils.isNotBlank(model.getName())) {
                Optional<Vendor> vendor = vendorService.findByName((model.getName()));
                if (vendor.isPresent()) {
                    throw new BusinessRuleException("This vendor name is already in use. Please check and provide a different one.");
                }
            }
        }
    }
    private void validId(final Integer id) {
        if (Objects.nonNull(id)) {
            vendorService.existsById(id);
        }
    }
    /*
     * Builders
     */
    private Vendor build(final Vendor model) {
        final AtomicReference<Vendor> built = new AtomicReference<>(model);
        List<VendorContact> vendorContacts = buildContact(built.get());
        List<VendorLocation> location = buildLocation(built.get());
        built.set(buildCreationData(built.get()));
        built.set(buildModificationData(built.get()));
        built.set(buildData(built.get(), vendorContacts, location));
        return built.get();
    }
    private Vendor buildData(final Vendor model, final List<VendorContact> vendorContacts,
                             final List<VendorLocation> vendorLocations) {
        if (!Objects.isNull(model)) {
            return model.withCompanyReg(model.getCompanyReg())
                    .withBillingAddress(model.getBillingAddress())
                    .withFinanceEmail(model.getFinanceEmail())
                    .withName(model.getName())
                    .withVat(model.getVat())
                    .withPaymentDetails(model.getPaymentDetails())
                    .withCompanyReg(model.getCompanyReg())
                    .withPaymentMethod(model.getPaymentMethod())
                    .withVendorLocations(vendorLocations)
                    .withVendorContacts(vendorContacts);
        }
        return model;
    }
    private Vendor buildCreationData(final Vendor model) {
        if (Objects.isNull(model.getCreationTime())) {
            return model
                    .withCreationTime(LocalDateTime.now())
                    .withCreationUser(securityManagementService.findCurrentUser());
        }
        return model;
    }
    private Vendor buildModificationData(final Vendor model) {
        if (!Objects.isNull(model.getId())) {
            return model
                    .withModificationTime(LocalDateTime.now())
                    .withModificationUser(securityManagementService.findCurrentUser());
        }
        return model;
    }
    private Customer buildStatus(final Customer model) {
        if (Objects.isNull(model.getStatus())) {
            return model.withStatus(CustomerStatus.builder().id(CustomerStatusEnum.ACTIVE.getId()).build());
        }
        return model;
    }
    private List<VendorContact> buildContact(final Vendor model) {//TODO:Refactorizar
        List<VendorContact> contactList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(model.getVendorContacts())) {
            model.getVendorContacts().forEach(vendorContact -> {
                if (Objects.isNull(vendorContact.getId())) {
                    contactList.add(contactService.save(vendorContact));
                } else {
                    VendorContact contact = contactService.findById(vendorContact.getId());
                    if (Objects.nonNull(contact)) {
                        contactList.add(contactService.save(getBuildUpdateVendor(vendorContact, contact)));
                    }
                }
            });
        }
        return contactList;
    }
    private List<VendorLocation> buildLocation(final Vendor model) {
        List<VendorLocation> vendorLocations = new ArrayList<>();
        if (!CollectionUtils.isEmpty(model.getVendorLocations())) {
            model.getVendorLocations().forEach(vendorLocation -> {
                if (Objects.isNull(vendorLocation.getId())) {
                    vendorLocations.add(locationService.save(vendorLocation));
                } else {
                    VendorLocation location = locationService.findById(vendorLocation.getId());
                    if (Objects.nonNull(location)) {
                        vendorLocations.add(locationService.save(VendorLocation
                                .builder()
                                .address(vendorLocation.getAddress())
                                .name(vendorLocation.getName())
                                .id(location.getId())
                                .build()));
                    }
                }
            });
        }
        return vendorLocations;
    }
}
