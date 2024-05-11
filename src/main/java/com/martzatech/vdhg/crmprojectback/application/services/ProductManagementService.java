package com.martzatech.vdhg.crmprojectback.application.services;

import com.martzatech.vdhg.crmprojectback.application.enums.DeletedStatusEnum;
import com.martzatech.vdhg.crmprojectback.application.exceptions.BusinessRuleException;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.File;
import com.martzatech.vdhg.crmprojectback.domains.chat.services.FileService;
import com.martzatech.vdhg.crmprojectback.domains.customers.enums.NoteTypeEnum;
import com.martzatech.vdhg.crmprojectback.domains.customers.mappers.ProductLocationMapper;
import com.martzatech.vdhg.crmprojectback.domains.customers.mappers.ProductMapper;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.*;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.*;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.ProductSpecification;
import com.martzatech.vdhg.crmprojectback.domains.security.models.DeletedStatus;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import com.martzatech.vdhg.crmprojectback.domains.security.services.SubsidiaryService;
import com.martzatech.vdhg.crmprojectback.domains.vendors.models.Vendor;
import com.martzatech.vdhg.crmprojectback.domains.vendors.services.VendorService;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.FileApiMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants.AZURE_PATH;
import static com.martzatech.vdhg.crmprojectback.application.helper.FileHelper.validateExtensionFile;

@AllArgsConstructor
@Slf4j
@Service
public class ProductManagementService {

    public static final int COUNT_ACTIVE_PRODUCTFILE = 1;
    private final ProductService productService;
    private final CategoryService categoryService;
    private final SubsidiaryService subsidiaryService;
    private final MembershipService membershipService;
    private final SecurityManagementService securityManagementService;
    private final AzureFileService azureFileService;
    private final ProductLocationService productLocationService;
    private final VendorService vendorService;
    //private final ProductFileService productFileService;
    private final NoteService noteService;
    private final ProductMapper productMapper;
    private final FileService fileService;
    private final CustomerService customerService;
    private final FileApiMapper fileApiMapper;
    private final ProductLocationMapper productLocationMapper;

    private static Product getBuild(Product productSave) {
        return Product
                .builder()
                .id(productSave.getId())
                .marketing(Objects.nonNull(productSave.getMarketing()) ? productSave.getMarketing() : null)
                .description(Objects.nonNull(productSave.getDescription()) ? productSave.getDescription() : null)
                .subsidiary(Objects.nonNull(productSave.getSubsidiary()) ? productSave.getSubsidiary() : null)
                .active(Objects.nonNull(productSave.getActive()) ? productSave.getActive() : null)
                .basePrice(Objects.nonNull(productSave.getBasePrice()) ? productSave.getBasePrice() : null)
                .subCategories(!CollectionUtils.isEmpty(productSave.getSubCategories()) ?
                        productSave.getSubCategories() : null)
                .visibility(Objects.nonNull(productSave.getVisibility()) ? productSave.getVisibility() : null)
                .memberships(Objects.nonNull(productSave.getMemberships()) ? productSave.getMemberships() : null)
                .defaultVat(Objects.isNull(productSave.getDefaultVat()) ? null : productSave.getDefaultVat())
                .defaultCommission(Objects.isNull(productSave.getDefaultCommission()) ? null : productSave.getDefaultCommission())
                .productVat(Objects.nonNull(productSave.getProductVat()) ? productSave.getProductVat() : null)
                .availabilityTo(Objects.nonNull(productSave.getAvailabilityTo()) ? productSave.getAvailabilityTo() : null)
                .productCommission(Objects.nonNull(productSave.getProductCommission()) ? productSave.getProductCommission() : null)
                .priceVisible(productSave.getPriceVisible())
                .files(!CollectionUtils.isEmpty(productSave.getFiles()) ? productSave.getFiles() : null)
                .name(Objects.nonNull(productSave.getName()) ? productSave.getName() : null)
                .availabilityFrom(Objects.nonNull(productSave.getAvailabilityFrom()) ? productSave.getAvailabilityFrom() : null)
                .category(Objects.nonNull(productSave.getCategory()) ? productSave.getCategory() : null)
                .creationTime(Objects.nonNull(productSave.getCreationTime()) ? productSave.getCreationTime() : null)
                .creationUser(Objects.nonNull(productSave.getCreationUser()) ? productSave.getCreationUser() : null)
                .modificationTime(Objects.nonNull(productSave.getModificationTime()) ? productSave.getModificationTime() : null)
                .modificationUser(Objects.nonNull(productSave.getModificationUser()) ? productSave.getModificationUser() : null)
                .locations(!CollectionUtils.isEmpty(productSave.getLocations()) ? productSave.getLocations() : null)
                .status(Objects.nonNull(productSave.getStatus()) ? productSave.getStatus() : null)
                .vendor(Objects.nonNull(productSave.getVendor()) ? productSave.getVendor() : null)
                .build();
    }

    private static void validateInstance(final Product product) {
        if (Objects.isNull(product)) {
            throw new BusinessRuleException("Product object is required");
        }
    }

    private static ProductFile buildProductFile(File archiveSave) {
        ProductFile productFile;
        productFile = ProductFile.builder()
                .products(archiveSave.getProducts())
                .extension(archiveSave.getExtension())
                .url(archiveSave.getUrl())
                .creationUser(archiveSave.getCreationUser())
                .creationTime(archiveSave.getCreationTime())
                .status(archiveSave.getStatus())
                .id(archiveSave.getId())
                .removalTime(archiveSave.getRemovalTime())
                .name(archiveSave.getName())
                .build();
        return productFile;
    }

    @Transactional
    public Product save(final Product product) {
        validations(product);
        //ValidarFormato
        List<ProductLocation> locations = buildLocation(product);
        return getBuild(getProductSave(product, locations));
    }

    private List<ProductLocation> buildLocation(final Product model) {
        List<ProductLocation> productLocation = new ArrayList<>();
        if (!CollectionUtils.isEmpty(model.getLocations())) {
            model.getLocations().forEach(location -> {
                if (Objects.isNull(location.getId())) {
                    productLocation.add(productLocationService.save(ProductLocation
                            .builder()
                            .address(location.getAddress())
                            .mapUrl(location.getMapUrl())
                            .longitude(location.getLongitude())
                            .latitude(location.getLatitude())
                            .placeId(location.getPlaceId())

                            .build()));
                } else {
                    ProductLocation locationResult = productLocationService.findById(location.getId());
                    if (Objects.nonNull(locationResult)) {
                        productLocation.add(productLocationService.save(ProductLocation
                                .builder()
                                .address(location.getAddress())
                                .mapUrl(location.getMapUrl())
                                .id(locationResult.getId())
                                .longitude(location.getLongitude())
                                .latitude(location.getLatitude())
                                .placeId(location.getPlaceId())
                                .build()));
                    }
                }
            });
        }
        return productLocation;
    }

    private Product getProductSave(Product product, List<ProductLocation> locations) {
        return productService.save(
                build(product, locations)
                        .withSubCategories(
                                CollectionUtils.isEmpty(product.getSubCategories())
                                        ? new ArrayList<>()
                                        : product.getSubCategories()
                        )
                        .withCategory(Objects.nonNull(product.getCategory()) ? product.getCategory() : null)
                        .withMemberships(
                                CollectionUtils.isEmpty(product.getMemberships())
                                        ? new ArrayList<>()
                                        : product.getMemberships()
                        )
                        .withStatus(
                                Objects.isNull(product.getStatus())
                                        ? DeletedStatus.builder().id(DeletedStatusEnum.ACTIVE.getId())
                                        .name("Active").build()
                                        : product.getStatus()
                        )
        );
    }

    private void validations(final Product product) {
        validateInstance(product);
        validateCategory(product);
        validateSubCategories(product);
        validateSubsidiary(product);
        validateMembership(product);
        validateDates(product);
        validateName(product);
        validateLocation(product);
    }

    private void validateLocation(Product product) {
        if (CollectionUtils.isEmpty(product.getLocations())) {
            throw new BusinessRuleException("When the Product Location list is passed, its location is required");
        }
    }

    private void validateName(final Product model) {
        final Product product = productService.findByName(model.getName());
        if (Objects.nonNull(product)
                && (Objects.isNull(model.getId()) || product.getId().intValue() != model.getId())) {
            throw new BusinessRuleException("Already exists a product with this name");
        }
    }

    private void validateDates(final Product product) {
        if (Objects.nonNull(product.getAvailabilityFrom())
                && Objects.nonNull(product.getAvailabilityTo())
                && product.getAvailabilityFrom().isAfter(product.getAvailabilityTo())) {
            throw new BusinessRuleException("Availability to should be greater than availability from");
        }
    }

    private void validateMembership(final Product product) {
        if (!CollectionUtils.isEmpty(product.getMemberships())) {
            product.getMemberships().forEach(
                    membership -> {
                        if (Objects.isNull(membership.getId())) {
                            throw new BusinessRuleException("When the membership object is passed, its id is required");
                        }
                        membershipService.existsById(membership.getId());
                    }
            );
        }
    }

    private void validateSubsidiary(final Product product) {
        if (Objects.nonNull(product.getSubsidiary())) {
            if (Objects.isNull(product.getSubsidiary().getId())) {
                throw new BusinessRuleException("When the subsidiary object is passed, its id is required");
            }
            subsidiaryService.existsById(product.getSubsidiary().getId());
        }
    }

    private void validateSubCategories(final Product product) {
        if (!CollectionUtils.isEmpty(product.getSubCategories())) {
            if (Objects.isNull(product.getCategory())) {
                throw new BusinessRuleException("When the subCategories list is passed, its category is required");
            }
            final Category category = categoryService.findById(product.getCategory().getId());
            final List<Integer> ids = category.getSubCategories().stream().map(SubCategory::getId).toList();
            product.getSubCategories().forEach(subCategory -> {
                if (Objects.isNull(subCategory.getId())) {
                    throw new BusinessRuleException("When the subCategory object is passed, its id is required");
                }
                if (!ids.contains(subCategory.getId())) {
                    throw new BusinessRuleException(
                            String.format(
                                    "SubCategory Id %d not belong to he category id %d",
                                    subCategory.getId(),
                                    category.getId()
                            )
                    );
                }
            });
        }
    }

    private void validateCategory(final Product product) {
        if (Objects.nonNull(product.getCategory())) {
            if (Objects.isNull(product.getCategory().getId())) {
                throw new BusinessRuleException("When the category object is passed, its id is required");
            }
            categoryService.existsById(product.getCategory().getId());
        }
    }

    private Product build(final Product product, final List<ProductLocation> locations) {
        Vendor vendor = null;

        if (Objects.requireNonNullElse(Objects.nonNull(product.getVendor()) ? product.getVendor().getVendorId() : null, 0) != 0) {
            vendorService.existsById(product.getVendor().getVendorId());
            vendor = vendorService.findById(product.getVendor().getVendorId());
        }

        final Product fromDDBB =
                Objects.nonNull(product.getId())
                        ? productService.findById(product.getId())
                        .withName(product.getName())
                        .withDescription(product.getDescription())
                        .withMarketing(product.getMarketing())
                        .withCategory(product.getCategory())
                        .withSubsidiary(product.getSubsidiary())
                        .withAvailabilityFrom(product.getAvailabilityFrom())
                        .withAvailabilityTo(product.getAvailabilityTo())
                        .withVisibility(product.getVisibility())
                        .withDefaultVat(product.getDefaultVat() == null ? null : product.getDefaultVat())
                        .withDefaultCommission(product.getDefaultCommission() == null ? null : product.getDefaultCommission())
                        .withProductVat(product.getDefaultVat() == Boolean.TRUE
                                ? Objects.nonNull(vendor) && Objects.nonNull(vendor.getVat())
                                ? vendor.getVat() : null
                                : product.getProductVat())
                        .withProductCommission(product.getDefaultCommission() == Boolean.TRUE
                                ? Objects.nonNull(vendor) && Objects.nonNull(vendor.getCommission())
                                ? vendor.getCommission() : null
                                : product.getProductCommission())
                        .withPriceVisible(product.getPriceVisible())
                        .withBasePrice(product.getBasePrice())
                        .withSubCategories(
                                CollectionUtils.isEmpty(product.getSubCategories())
                                        ? new ArrayList<>()
                                        : product.getSubCategories()
                        )
                        .withLocations(!CollectionUtils.isEmpty(product.getLocations()) ? locations : null)
                        .withVendor(Objects.nonNull(product.getVendor()) ? product.getVendor() : null)
                        .withMemberships(
                                CollectionUtils.isEmpty(product.getMemberships())
                                        ? new ArrayList<>()
                                        : product.getMemberships()
                        )
                        .withVisibility(
                                Objects.isNull(product.getVisibility())
                                        ? Boolean.TRUE
                                        : product.getVisibility()
                        )
                        : productService.save(product.withLocations(locations));
        return fromDDBB
                .withDefaultVat(fromDDBB.getDefaultVat())
                .withDefaultCommission(fromDDBB.getDefaultCommission())
                .withProductVat(fromDDBB.getDefaultVat() == Boolean.TRUE ? Objects.nonNull(vendor) ?
                        vendor.getVat() : null : fromDDBB.getProductVat())
                .withProductCommission(fromDDBB.getDefaultCommission() == Boolean.TRUE ?
                        Objects.nonNull(vendor) ? vendor.getCommission() : null : fromDDBB.getProductCommission())
                .withPriceVisible(product.getPriceVisible())
                .withBasePrice(product.getBasePrice())
                .withVendor(Objects.nonNull(product.getVendor()) ? product.getVendor() : null)
                .withLocations(!CollectionUtils.isEmpty(product.getLocations()) ? locations : null)
                .withMemberships(product.getMemberships())
                .withCreationTime(
                        Objects.isNull(product.getId())
                                ? LocalDateTime.now()
                                : fromDDBB.getCreationTime()
                ).withCreationUser(
                        Objects.isNull(product.getId())
                                ? securityManagementService.findCurrentUser()
                                : fromDDBB.getCreationUser()
                ).withModificationTime(
                        Objects.nonNull(product.getId())
                                ? LocalDateTime.now()
                                : null
                ).withModificationUser(
                        Objects.nonNull(product.getId())
                                ? securityManagementService.findCurrentUser()
                                : null
                );
    }

    @Transactional
    public List<Product> findAll(final ProductSpecification specification,
                                 final Integer page, final Integer size, final String direction,
                                 final String attribute) {
        final Direction directionEnum =
                Arrays.stream(Direction.values()).anyMatch(v -> v.name().equalsIgnoreCase(direction))
                        ? Direction.fromString(direction)
                        : Direction.DESC;
        final Sort sort = Sort.by(directionEnum, attribute);
        final Pageable pageable = PageRequest.of(page, size, sort);
        return productService.findAll(specification, pageable)
                .stream()
                .map(product -> product.withFiles(fileService.getFileProduct(product.getId())))
                .collect(Collectors.toList());
    }

    public Long count(final ProductSpecification specification) {
        return productService.count(specification);
    }

    @Transactional
    public Product findById(final Integer id) {
        return productService.findById(id);
    }

    public void deleteById(final Integer id) {
        productService.deleteById(id);
    }

    public void deleteStatus(final Integer id) {
        productService.deleteStatus(id);
        deleteNoteAssociateProduct(id);
    }

    private void deleteNoteAssociateProduct(Integer id) {
        List<Note> notes = noteService.finByElementId(id);
        if (Objects.nonNull(notes)) {
            notes.forEach(note -> {
                noteService.deleteStatus(note.getId());
            });
        }
    }

    public void deleteByStatusAndId(final Integer id) {
        productService.deleteByStatusAndId(id);
    }

    @Transactional
    public ProductFile addPicture(final Integer id, final MultipartFile file, final String extension) {
        ProductFile productFile = null;
        productService.existsById(id);
        String extensionFile = FilenameUtils.getExtension(file.getOriginalFilename());
        validateExtensionFile(extension, extensionFile);
        File archiveSave = null;
        Product product = productService.findById(id);
        if (Objects.nonNull(product)) {
            archiveSave = getFileProduct(file, extension, securityManagementService.findCurrentUser(), product);
            if (Objects.nonNull(archiveSave)) {
                productFile = buildProductFile(archiveSave);
            }
        }
        return productFile;
    }

    private File getFileProduct(MultipartFile file, String extension,
                                User currentUser, Product product) {
        return fileService.save(
                File.builder()
                        .url(azureFileService.uploadFile(file, AZURE_PATH, extension))
                        .extension(extension)
                        .name(file.getOriginalFilename())
                        .products(List.of(product))
                        .status(DeletedStatus.builder().id(DeletedStatusEnum.ACTIVE.getId()).build())
                        .creationUser(Objects.isNull(currentUser) ? securityManagementService.findCurrentUser() : currentUser)
                        .creationTime(LocalDateTime.now())
                        .build()
        );
    }

    //TODO:Conversarlo para ticket
    public void deletePicture(final Integer id, final Integer fileId) {
        fileService.existsById(fileId);
       /* if (byId.getProducts().getId().intValue() != id) {
            throw new BusinessRuleException("This fileId not correspond to the product id");
        }*/
        fileService.deleteById(fileId);
        // validateExistMaxFile(fileId, byId);
    }

    public Note saveNote(final Integer id, final Note note) {
        productService.existsById(id);

        final User currentUser = securityManagementService.findCurrentUser();
        final Note toSave = Objects.nonNull(note.getId())
                ? getPersistedNote(id, note, currentUser)
                : note.withType(NoteTypeEnum.PRODUCT).withElementId(id);

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

        if (byId.getType() != NoteTypeEnum.PRODUCT) {
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
}
