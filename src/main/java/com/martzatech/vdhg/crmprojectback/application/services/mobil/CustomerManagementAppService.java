package com.martzatech.vdhg.crmprojectback.application.services.mobil;

import com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants;
import com.martzatech.vdhg.crmprojectback.application.enums.*;
import com.martzatech.vdhg.crmprojectback.application.exceptions.BusinessRuleException;
import com.martzatech.vdhg.crmprojectback.application.exceptions.FieldIsMissingException;
import com.martzatech.vdhg.crmprojectback.application.services.*;
import com.martzatech.vdhg.crmprojectback.domains.chat.enums.ChatMessageTypeEnum;
import com.martzatech.vdhg.crmprojectback.domains.chat.enums.ChatRoomTypeEnum;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.ChatMessage;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.ChatRoom;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.File;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.FileWallet;
import com.martzatech.vdhg.crmprojectback.domains.chat.services.ChatMessageService;
import com.martzatech.vdhg.crmprojectback.domains.chat.services.ChatRoomService;
import com.martzatech.vdhg.crmprojectback.domains.chat.services.FileService;
import com.martzatech.vdhg.crmprojectback.domains.commons.models.*;
import com.martzatech.vdhg.crmprojectback.domains.customers.enums.NoteTypeEnum;
import com.martzatech.vdhg.crmprojectback.domains.customers.enums.OfferStatusEnum;
import com.martzatech.vdhg.crmprojectback.domains.customers.mappers.GroupAccountMapper;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.*;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.*;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.CustomerSpecification;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.WalletSpecification;
import com.martzatech.vdhg.crmprojectback.domains.security.mappers.UserMapper;
import com.martzatech.vdhg.crmprojectback.domains.security.models.DeletedStatus;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import com.martzatech.vdhg.crmprojectback.domains.security.models.UserMobil;
import com.martzatech.vdhg.crmprojectback.domains.security.models.UserStatus;
import com.martzatech.vdhg.crmprojectback.domains.security.services.RoleService;
import com.martzatech.vdhg.crmprojectback.domains.security.services.UserService;
import com.martzatech.vdhg.crmprojectback.domains.wallet.models.Wallet;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.FileRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants.AZURE_PATH;
import static com.martzatech.vdhg.crmprojectback.application.helper.FileHelper.validateExtensionFile;

@AllArgsConstructor
@Slf4j
@Service
public class CustomerManagementAppService {

    public static final int ID_ROL_CUSTOMER = 4;
    public static final int ID_USER_SYSTEM = 2;
    private final CustomerAppService customerService;
    private final CompanyService companyService;
    private final PersonManagementService personManagementService;
    private final MembershipService membershipService;
    private final CustomerStatusService customerStatusService;
    private final LeadService leadService;
    private final SecurityManagementService securityManagementService;
    private final SecurityManagementAppService securityManagementAppService;

    private final AzureFileService azureFileService;
    private final NoteService noteService;
    private final ChatManagementService chatManagementService;
    private final ChatRoomService roomService;
    private final RoleService roleService;
    private final ChatMessageService messageService;
    private final UserService userService;
    private final FileService fileService;
    private final UserMapper userMapper;
    private final OfferService offerService;
    private final AssociatedService associatedService;
    private final GroupAccountMapper accountMapper;
    private final WalletManagementService walletManagementService;

    /*
     * The core services
     */

    public List<FileWallet> getWallet( WalletSpecification specification,
                                      Pageable pageable,String token){
        Associated associated;
        Customer customer;
        User user = null;
        user = getUserValidate(token,user);
        List<FileWallet> files = new ArrayList<>();
        if (user.getTypeUser().equals(UserTypeEnum.CUSTOMER.name())) {
            customer = customerService.findById(user.getCustomer().getId(), token);
            final Person person = customerService.findById(customer.getId(),token).getPerson();
            List<Wallet> wallets =   walletManagementService.findByPerson(person,pageable);
            if(!CollectionUtils.isEmpty(wallets)){
                files = getFileList(wallets);
            }
        }else{
            associated = associatedService.findById(user.getAssociated().getId());
            final Person person = customerService.findById(associated.getGroupAccount()
                    .getOwner().getId(),token).getPerson();
            List<Wallet> wallets =   walletManagementService.findByPerson(person,pageable);
            if(!CollectionUtils.isEmpty(wallets)){
                files = getFileList(wallets);
            }
        }
        return files;
    }

    private User getUserValidate(String token, User user) {
        if (securityManagementAppService.validateToken(token)) {
            user = securityManagementAppService.findCurrentUser(token);
        }
        return user;
    }

    private static List<FileWallet> getFileList(List<Wallet> wallets) {
        return wallets.stream().map(wallet -> FileWallet
                        .builder()
                        .id(wallet.getFile().getId())
                        // .text(Objects.nonNull(wallet.getFile().getText()) ? wallet.getFile().getText() : null)
                        .url(Objects.nonNull(wallet.getFile().getUrl()) ? wallet.getFile().getUrl() : null)
                        .name(Objects.nonNull(wallet.getFile().getName())?wallet.getFile().getName() :null)
                        .extension(Objects.nonNull(wallet.getFile().getExtension())?wallet.getFile().getExtension() :null)
                        .creationTime(Objects.nonNull(wallet.getFile().getCreationTime()) ? wallet.getFile().getCreationTime() : null)
                        .creationUser(Objects.nonNull(wallet.getFile().getCreationUser()) ? User.builder()
                                .id(wallet.getFile().getCreationUser() .getId())
                                .title(Objects.nonNull(wallet.getFile().getCreationUser() .getTitle())?wallet.getFile().getCreationUser() .getTitle():null)
                                .name(Objects.nonNull(wallet.getFile().getCreationUser().getName())?wallet.getFile().getCreationUser().getName():null)
                                .surname(Objects.nonNull(wallet.getFile().getCreationUser().getSurname())?wallet.getFile().getCreationUser().getSurname():null)
                                .build(): null).build())
                .collect(Collectors.toList());
    }

    @Transactional
    public Customer saveCustomer(final Customer model, final String token) {
        validations(model, token);
        Customer saveCustomer = customerService.save(build(model)
                .withDeletedStatus(DeletedStatus.builder().id(1).build()));

        if (Objects.nonNull(saveCustomer)) {
            User member = securityManagementService.saveUser(buildUserCustomer(saveCustomer,
                    userService.findByIdCustomer(saveCustomer.getId())).withStatus(UserStatus.builder().id(1).build()));
            securityManagementAppService.saveUserMobil(buildUserMobil(saveCustomer));
            if (Objects.nonNull(member) && Objects.isNull(model.getId())) {
                if (member.getTypeUser().equals(UserTypeEnum.CUSTOMER.name())) {
                    ChatRoom chatRoom = saveCustomerChatRoom(member);
                    buildMessageCustomerChat(chatRoom, member);
                }
            }
        }
        return saveCustomer;
    }


    private UserMobil buildUserMobil(Customer saveCustomer) {
        User userCustomer = userService.findByIdCustomer(saveCustomer.getId());
        return UserMobil.builder()
                .userName(userCustomer.getEmail())
                .password(generateSHA256Hash(saveCustomer.getPerson().getIdentityDocuments()
                        .stream()
                        .map(identityDocument -> identityDocument.getValue()).findAny().get()))
                .user(userMapper.modelToModelNew(userCustomer))
                .passwordUpdateRequired(false)
                .build();
    }

    public static String generateSHA256Hash(String password) {
        try {
            // Obtén una instancia de MessageDigest con el algoritmo SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Convierte la contraseña en bytes
            byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);

            // Calcula el hash SHA-256 de los bytes de la contraseña
            byte[] hashBytes = digest.digest(passwordBytes);

            // Convierte el hash en una representación hexadecimal
            StringBuilder hexHash = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) {
                    hexHash.append('0');
                }
                hexHash.append(hex);
            }

            return hexHash.toString();
        } catch (NoSuchAlgorithmException e) {
            // Manejo de excepción en caso de algoritmo no encontrado
            e.printStackTrace();
            return null;
        }
    }

    private void buildMessageCustomerChat(ChatRoom chatRoom, User member) {

        if (Objects.nonNull(chatRoom)) {
            messageService.save(ChatMessage.builder().chatRoom(chatRoom)
                    .value("Welcome " + member.getName() +
                            " to the Concierge Chat. Please feel free" +
                            " to ask anything you need").sender(securityManagementService
                            .findUserById(ID_USER_SYSTEM)).type(ChatMessageTypeEnum.SYSTEM).build());
        }
    }

    private ChatRoom saveCustomerChatRoom(User user) {
        validateCustomerChatRoomExist(user.getId());
        return chatManagementService.saveRoom(ChatRoom
                .builder()
                .name(user.getName() + " " + user.getSurname())
                .members(List.of(user))
                .type(ChatRoomTypeEnum.CUSTOMER)
                .groupAccount(user.getCustomer().getGroupAccount())
                .customer(user.getCustomer())
                .build());
    }

    private void validateCustomerChatRoomExist(final Integer id) {
        List<ChatRoom> chatRooms = chatManagementService.findAllChatRoomMember(id);
        if (chatRooms.size() > 0) {
            throw new BusinessRuleException("It can only contain one Customer per chat.");
        }
    }


    @Transactional
    public Customer saveLead(final Customer model, final String token) {
        validations(model, token);
        Customer saveCustomer = customerService.save(build(model)
                .withDeletedStatus(DeletedStatus.builder().id(1).build()));
        if (Objects.nonNull(saveCustomer)) {
            User userCustomer = userService.findByIdCustomer(saveCustomer.getId());
            User member = securityManagementService.saveUser(buildUserCustomer(saveCustomer, userCustomer)
                    .withStatus(UserStatus.builder().id(1).build()));
            if (Objects.nonNull(member)) {
                if (member.getTypeUser().equals(UserTypeEnum.CUSTOMER.name())) {
                    ChatRoom chatRoom = saveCustomerChatRoom(member);
                    buildMessageCustomerChat(chatRoom, member);
                }

            }
        }
        return saveCustomer;
    }

    /**
     * buildUserCustomer User save user
     *
     * @param model
     * @return
     */
    public User buildUserCustomer(final Customer model, User user) {
        return User.builder()
                .id(Objects.nonNull(user) ? user.getId() : null)
                .title(model.getPerson().getTitle().getName())
                .name(model.getPerson().getName())
                .surname(model.getPerson().getSurname())
                .mobile(model.getPerson().getPhones().get(0).getValue())
                .roles(List.of(roleService.findById(3)))
                .typeUser(UserTypeEnum.CUSTOMER.name())
                .email(model.getPerson().getEmails().get(0).getValue())
                .customer(model)
                .status(Objects.isNull(model.getStatus())
                        ? UserStatus.builder().id(UserStatusEnum.ACTIVE.getId()).build()
                        : model.getCreationUser().getStatus())
                .creationUser(Objects.nonNull(model.getCreationUser())
                        ? model.getCreationUser()
                        : securityManagementService.findCurrentUser())
                .creationTime(Objects.nonNull(model.getCreationTime())
                        ? model.getCreationTime()
                        : LocalDateTime.now())
                .modificationUser(Objects.isNull(model.getId())
                        ? null
                        : securityManagementService.findCurrentUser())
                .modificationTime(Objects.isNull(model.getId())
                        ? null
                        : LocalDateTime.now())
                .build();
    }

    /**
     * @param id
     * @param file
     * @param extension
     * @param name
     * @return
     */
    public File addFile(final Integer id, final MultipartFile file, final String extension,
                        final String name, String token) {
        String extensionFile = FilenameUtils.getExtension(file.getOriginalFilename());
        validateExtensionFile(extension, extensionFile);
        validId(id);
        Customer customer = customerService.findById(id, token);
        String urlFile = azureFileService.uploadFile(file, AZURE_PATH, extension);
        log.info("Save File Customer id:{}", id);
        File leadCustomerFile = fileService.save(
                File.builder()
                        .url(urlFile)
                        .extension(extension)
                        .name(!name.equals("") ? name : extractFileNameWithoutExtension(file.getOriginalFilename()))
                        .persons(Objects.nonNull(customer) ? List.of(customer.getPerson()) : null)
                        .status(DeletedStatus.builder().id(DeletedStatusEnum.ACTIVE.getId()).build())
                        .creationUser(securityManagementService.findCurrentUser())
                        .creationTime(LocalDateTime.now())
                        .build()
        );
        //getFileCustomerLead(file, extension, securityManagementService.findCurrentUser(), customer);
        return leadCustomerFile;
    }

    private File getFileCustomerLead(MultipartFile file, String extension, User currentUser,
                                     Customer leadCustomerFile) {
        File messageFile = null;
        Customer customer = leadCustomerFile;
        if (Objects.nonNull(file)) {
            String urlFile = azureFileService.uploadFile(file, AZURE_PATH, extension);
            messageFile = fileService.save(
                    File.builder()
                            .url(urlFile)
                            .extension(extension)
                            .name(extractFileNameWithoutExtension(file.getOriginalFilename()))
                            .persons(Objects.nonNull(customer) ? List.of(customer.getPerson()) : null)
                            .status(DeletedStatus.builder().id(DeletedStatusEnum.ACTIVE.getId()).build())
                            .creationUser(Objects.isNull(currentUser) ? securityManagementService.findCurrentUser() : currentUser)
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

    public Customer convert(final Customer model, final Integer leadId, final String token) {
        final Lead lead = leadService.findById(leadId);
        final Customer saved = saveLead(
                model
                        .withLead(lead)
                        .withDeletedStatus(DeletedStatus.builder().id(DeletedStatusEnum.ACTIVE.getId()).build())
                        .withCreationType(CreationType.builder().id(CustomerCreationTypeEnum.CONVERTED.getId()).build()), token
        );
        leadService.save(lead
                .withStatus(LeadStatus.builder().id(LeadStatusEnum.CONVERTED.getId()).build())
                .withModificationTime(LocalDateTime.now()));
        return saved;
    }

    public void changeStatus(final Integer id, final Integer statusId, final String token) {
        final Customer model = customerService.findById(id, token);
        final CustomerStatus customerStatus = customerStatusService.findById(statusId);
        changeStatusUser(customerStatus, model);
        customerService.save(
                model
                        .withStatus(customerStatus)
                        .withModificationTime(LocalDateTime.now())
                        .withModificationUser(securityManagementService.findCurrentUser())
        );
    }

    private void changeStatusUser(CustomerStatus customerStatus, Customer model) {
        List<Associated> associatedList = new ArrayList<>();
        User userCustomer = userService.findByIdCustomer(model.getId());
        if (Objects.nonNull(model.getGroupAccount())) {
            associatedList = associatedService
                    .findByAssociatedGroupAccount(accountMapper.modelToEntity(model.getGroupAccount()));
        }

        if (customerStatus.getId().equals(CustomerStatusEnum.ACTIVE.getId())) {
            userService.activeUser(userCustomer.getId());
            if (!CollectionUtils.isEmpty(associatedList)) {
                associatedList.forEach(associated -> {
                    var userAssociated = userService.findByIdAssociated(associated.getId());
                    userService.activeUser(userAssociated.getId());
                });
            }
        } else {
            userService.deleteByStatusAndId(userCustomer.getId());
            if (!CollectionUtils.isEmpty(associatedList)) {
                associatedList.forEach(associated -> {
                    var userAssociated = userService.findByIdAssociated(associated.getId());
                    userService.deleteByStatusAndId(userAssociated.getId());
                });
            }
        }
    }


    public List<Customer> findAll(final CustomerSpecification specification, final String token,
                                  final Integer page, final Integer size, final String direction,
                                  final String attribute) {
        final Direction directionEnum =
                Arrays.stream(Direction.values()).anyMatch(v -> v.name().equalsIgnoreCase(direction))
                        ? Direction.fromString(direction)
                        : Direction.DESC;
        final Sort sort = Sort.by(directionEnum, attribute);
        final Pageable pageable = PageRequest.of(page, size, sort);
        return customerService.findAll(specification, token, pageable).stream()
                .map(customer -> customer.withChatRoom(roomService.findByIdCustomer(customer.getId())))
                .map(customer -> customer.withFiles(fileService.getFilesCustomerNotPageable(customer.getId())))
                .collect(Collectors.toList());
    }

    public void deleteById(final Integer id) {
        List<File> files = fileService.getFilesCustomerNotPageable(id);
        checkIfHasAssociatedOffer(id);
        if (!CollectionUtils.isEmpty(files)) {
            files.forEach(file -> fileService.deleteById(file.getId()));
        }
        customerService.deleteById(id);
    }

    public void deleteCustomerFile(final Integer idCustomer, final Integer idFile) {
        customerService.existsById(idCustomer);
        checkIfHasAssociatedOffer(idCustomer);
        fileService.deleteById(idFile);
    }

    private void checkIfHasAssociatedOffer(final Integer customerId) {
        final List<Offer> byOffer = offerService.findByCustomerId(customerId);
        if (!CollectionUtils.isEmpty(byOffer)) {
            byOffer.forEach(offer -> {
                if (offer.getStatus().equals(OfferStatusEnum.CONFIRMED)
                        || offer.getStatus().equals(OfferStatusEnum.ACCEPTED)) {
                    if (!CollectionUtils.isEmpty(offer.getFiles())) {
                        final String message = String
                                .format(
                                        "The offer id %s, number %s, version %s cannot be deleted. "
                                                + "It is related to %s offers",
                                        offer.getId(), offer.getNumber(), offer.getVersion(),
                                        byOffer.size()
                                );
                        throw new BusinessRuleException(message);
                    }
                }
            });
        }
    }


    public void deleteByStatus(final Integer id) {
        customerService.deleteByStatus(id);
        deleteNoteAssociateCustomer(id);
        User user = userService.findByIdCustomer(id);
        if (Objects.nonNull(user)) {
            deleteChatAssociate(user.getId());
            userService.deleteByStatusAndId(user.getId());
        }
    }

    private void deleteChatAssociate(Integer id) {
        List<ChatRoom> chatRooms = chatManagementService.findAllChatRoomMember(id);
        if (!CollectionUtils.isEmpty(chatRooms)) {
            chatRooms.forEach(chatRoom -> roomService.deleteChat(chatRoom.getId()));
        }
    }

    private void deleteNoteAssociateCustomer(Integer id) {
        List<Note> notes = noteService.finByElementId(id);
        if (Objects.nonNull(notes)) {
            notes.forEach(note -> {
                noteService.deleteStatus(note.getId());
            });
        }
    }
    /*
     * The identity document services is directly related to the customer module
     */

    public IdentityDocument saveIdentityDocument(final Integer customerId, final String token, final IdentityDocument model) {
        final Customer byId = customerService.findById(customerId, token);
        return personManagementService.saveIdentityDocument(byId.getPerson().getId(), model);
    }

    public List<IdentityDocument> getIdentityDocuments(final Integer customerId, final String token) {
        final Person person = customerService.findById(customerId, token).getPerson();
        if (Objects.nonNull(person) && !CollectionUtils.isEmpty(person.getIdentityDocuments())) {
            return person.getIdentityDocuments();
        }
        return Collections.emptyList();
    }

    public void deleteIdentityDocumentById(final Integer customerId, final Integer id, final String token) {
        final Customer customer = customerService.findById(customerId, token);
        personManagementService.deleteIdentityDocumentById(customer.getPerson().getId(), id);
    }

    /*
     * The phone services is directly related to the leads module
     */

    public Phone savePhone(final Integer customerId, final Phone model, final String token) {
        final Customer customer = customerService.findById(customerId, token);
        return personManagementService.savePhone(customer.getPerson().getId(), model);
    }

    public List<Phone> getPhones(final Integer customerId, final String token) {
        final Person person = customerService.findById(customerId, token).getPerson();
        if (Objects.nonNull(person) && !CollectionUtils.isEmpty(person.getPhones())) {
            return person.getPhones();
        }
        return Collections.emptyList();
    }

    public void validatePhone(final Integer customerId, final Integer id, final Boolean valid, final String token) {
        final Customer customer = customerService.findById(customerId, token);
        personManagementService.validatePhone(customer.getPerson().getId(), id, valid);
    }

    public void deletePhoneById(final Integer customerId, final Integer id, final String token) {
        final Customer customer = customerService.findById(customerId, token);
        personManagementService.deletePhoneById(customer.getPerson().getId(), id);
    }

    /*
     * The email services is directly related to the leads module
     */

    public Email saveEmail(final Integer customerId, final Email model, final String token) {
        final Customer customer = customerService.findById(customerId, token);
        return personManagementService.saveEmail(customer.getPerson().getId(), model);
    }

    public List<Email> getEmails(final Integer customerId, final String token) {
        final Person person = customerService.findById(customerId, token).getPerson();
        if (Objects.nonNull(person) && !CollectionUtils.isEmpty(person.getEmails())) {
            return person.getEmails();
        }
        return Collections.emptyList();
    }

    public void validateEmail(final Integer customerId, final Integer id, final Boolean valid, final String token) {
        final Customer customer = customerService.findById(customerId, token);
        personManagementService.validateEmail(customer.getPerson().getId(), id, valid);
    }

    public void deleteEmailById(final Integer customerId, final Integer id, final String token) {
        final Customer customer = customerService.findById(customerId, token);
        personManagementService.deleteEmailById(customer.getPerson().getId(), id);
    }

    /*
     * The address services is directly related to the leads module
     */

    public Address saveAddress(final Integer customerId, final Address model, final String token) {
        final Customer customer = customerService.findById(customerId, token);
        return personManagementService.saveAddress(customer.getPerson().getId(), model);
    }

    public List<Address> getAddress(final Integer customerId, final String token) {
        final Person person = customerService.findById(customerId, token).getPerson();
        if (Objects.nonNull(person) && !CollectionUtils.isEmpty(person.getAddresses())) {
            return person.getAddresses();
        }
        return Collections.emptyList();
    }

    public void deleteAddressById(final Integer customerId, final Integer id, final String token) {
        final Customer customer = customerService.findById(customerId, token);
        personManagementService.deleteAddressById(customer.getPerson().getId(), id);
    }

    /*
     * Validators
     */
    private void validations(final Customer model, final String token) {
        validId(model.getId());
        validMembership(model.getMembership());
        validPerson(model, token);
        validIdentityDocuments(model.getPerson().getIdentityDocuments());
        validEmails(model);
        validPhones(model.getPerson().getPhones());
        validAddresses(model.getPerson().getAddresses());
        validResidence(model.getPerson().getResidence());
        //Validate rol
    }

    private void validId(final Integer id) {
        if (Objects.nonNull(id)) {
            customerService.existsById(id);
            log.info("Customer valid id {}", id);
        }
    }

    private void validResidence(final Country model) {
        if (Objects.isNull(model) || Objects.isNull(model.getId())) {
            throw new FieldIsMissingException(CommonConstants.RESIDENCE);
        }
    }

    private void validMembership(final Membership model) {
        if (Objects.isNull(model) || Objects.isNull(model.getId())) {
            throw new FieldIsMissingException(CommonConstants.MEMBERSHIP);
        }
        membershipService.existsById(model.getId());
    }

    private void validPerson(final Customer model, final String token) {
        if (Objects.isNull(model)) {
            throw new FieldIsMissingException(CommonConstants.PERSON);
        }
        if (Objects.nonNull(model.getId())
                && model.getId() != 0
                && (Objects.isNull(model.getPerson().getId()) || model.getPerson().getId() == 0)) {
            throw new BusinessRuleException(CommonConstants.WHEN_UPDATE_THE_PERSON_ID_IS_MANDATORY_MESSAGE);
        }
        if (Objects.nonNull(model.getId())) {
            final Customer customer = customerService.findById(model.getId(), token);
            if (model.getPerson().getId().intValue() != customer.getPerson().getId()) {
                throw new BusinessRuleException(CommonConstants.PERSON_ID_DOES_NOT_CORRESPOND_MESSAGE);
            }
        }
    }

    private void validIdentityDocuments(final List<IdentityDocument> identityDocuments) {
        if (CollectionUtils.isEmpty(identityDocuments)) {
            throw new FieldIsMissingException(CommonConstants.IDENTITY_DOCUMENTS);
        }
    }

    private void validEmails(final Customer model) {
        if (CollectionUtils.isEmpty(model.getPerson().getEmails())) {
            throw new FieldIsMissingException(CommonConstants.EMAILS);
        }
        model.getPerson().getEmails().forEach(email -> {
            if (StringUtils.isNotEmpty(email.getValue())) {
                final Customer byEmail = customerService.findByEmail(email.getValue());
                if (!Objects.isNull(byEmail)) {
                    if (Objects.isNull(model.getId()) || model.getId().intValue() != byEmail.getId()) {
                        throw new BusinessRuleException("Already exists a Customer with this email: " + email.getValue());
                    } else if (Objects.nonNull(email.getId())) {
                        byEmail.getPerson().getEmails().stream()
                                .filter(e -> e.getValue().equalsIgnoreCase(email.getValue()))
                                .findAny()
                                .ifPresent(item -> {
                                    if (email.getId().intValue() != item.getId()) {
                                        throw new BusinessRuleException("The customer already has this email: " + email.getValue());
                                    }
                                });
                    }
                }
            }
        });
    }

    private void validPhones(final List<Phone> phones) {
        if (CollectionUtils.isEmpty(phones)) {
            throw new FieldIsMissingException(CommonConstants.PHONES);
        }
    }

    private void validAddresses(final List<Address> addresses) {
        if (CollectionUtils.isEmpty(addresses)) {
            throw new FieldIsMissingException(CommonConstants.ADDRESSES);
        }
    }

    /*
     * Builders
     */
    private Customer build(final Customer model) {
        final AtomicReference<Customer> built = new AtomicReference<>(model);
        built.set(buildCompany(built.get()));
        built.set(buildPerson(built.get()));
        built.set(buildStatus(built.get()));
        built.set(buildCreationData(built.get()));
        built.set(buildModificationData(built.get()));
        built.set(buildCreationType(built.get()));
        return built.get();
    }

    private Customer buildPerson(final Customer model) {
        if (!Objects.isNull(model.getPerson())) {
            return model.withPerson(personManagementService.save(model.getPerson()));
        }
        return model;
    }

    private Customer buildCreationData(final Customer model) {
        if (Objects.isNull(model.getCreationTime())) {
            return model
                    .withCreationTime(LocalDateTime.now())
                    .withCreationUser(securityManagementService.findCurrentUser());
        }
        return model;
    }

    private Customer buildModificationData(final Customer model) {
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

    private Customer buildCompany(final Customer model) {
        if (!Objects.isNull(model.getCompany())) {
            if (Objects.isNull(model.getCompany().getId())) {
                if (StringUtils.isNoneEmpty(model.getCompany().getName())) {
                    final Optional<Company> byName = companyService.findByName(model.getCompany().getName());
                    if (byName.isPresent()) {
                        return model.withCompany(byName.get());
                    }
                    return model.withCompany(companyService.save(model.getCompany()));
                } else {
                    throw new FieldIsMissingException(CommonConstants.LEAD_COMPANY_FIELD);
                }
            }
            return model.withCompany(companyService.findById(model.getCompany().getId()));
        }
        return model;
    }

    private Customer buildCreationType(final Customer model) {
        if (Objects.isNull(model.getCreationType())) {
            return model
                    .withCreationType(CreationType.builder().id(CustomerCreationTypeEnum.MANUAL.getId()).build());
        }
        return model;
    }

    public Note saveNote(final Integer id, final Note note) {
        customerService.existsById(id);

        final User currentUser = securityManagementService.findCurrentUser();
        final Note toSave = Objects.nonNull(note.getId())
                ? getPersistedNote(id, note, currentUser)
                : note.withType(NoteTypeEnum.CUSTOMER).withElementId(id);

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

        if (byId.getType() != NoteTypeEnum.CUSTOMER) {
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
