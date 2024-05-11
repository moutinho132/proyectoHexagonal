package com.martzatech.vdhg.crmprojectback.application.services;

import com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants;
import com.martzatech.vdhg.crmprojectback.application.enums.DeletedStatusEnum;
import com.martzatech.vdhg.crmprojectback.application.enums.UserTypeEnum;
import com.martzatech.vdhg.crmprojectback.application.exceptions.BusinessRuleException;
import com.martzatech.vdhg.crmprojectback.domains.chat.enums.ChatMessageTypeEnum;
import com.martzatech.vdhg.crmprojectback.domains.chat.enums.ChatRoomTypeEnum;
import com.martzatech.vdhg.crmprojectback.domains.chat.mappers.ChatMessageReaderMapper;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.*;
import com.martzatech.vdhg.crmprojectback.domains.chat.repositories.ChatMessageReaderRepository;
import com.martzatech.vdhg.crmprojectback.domains.chat.services.*;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Customer;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.CustomerService;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.*;
import com.martzatech.vdhg.crmprojectback.domains.security.models.DeletedStatus;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import com.martzatech.vdhg.crmprojectback.domains.security.services.UserService;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.FileApiMapper;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.martzatech.vdhg.crmprojectback.application.helper.FileHelper.validateExtensionFile;
import static com.martzatech.vdhg.crmprojectback.application.helper.SpecificationHelper.addSoftDeleteChatOutOffice;

@AllArgsConstructor
@Slf4j
@Service
public class ChatManagementService {

    private static final String CREATION_PERMISSION = "CHATS_CREATE_ALL";
    private static final String CREATION_PERMISSION_OUTOFFICE = "CHATS_MESSAGE_OUTOFFICE_CREATE";
    private static final String INTERNAL_PERMISSION = "CHATS_VIEW_ALL";

    private static final String CHAT_ARCHIVE = "CHAT_ARCHIVE";

    private static final String CUSTOMER_PERMISSION = "CHATS_VIEW_OWN";
    private static final String CHAT_FILE = "CHATS_FILE";
    private static final String AZURE_PATH = "chats";
    private static final Sort DEFAULT_MESSAGE_SORT = Sort.by(Direction.DESC, "creationTime");
    private static final Sort DEFAULT_MESSAGE_ASC_SORT = Sort.by(Direction.ASC, "creationTime");

    private static final String CHAT_DEFAULT_NAME_PREFIX = "chat-";

    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;
    private final ChatMessageReaderService chatMessageReaderService;
    private final UserService userService;
    private final SecurityManagementService securityManagementService;
    private final AzureFileService azureFileService;
    private final ChatMessageOutOfficeService chatMessageOutOfficeService;
    private final FileService fileService;
    private final FileManagementService fileManagementService;
    private final CustomerService customerService;
    private final FileApiMapper fileMapper;
    private final ChatMessageReaderRepository chatMessageReaderRepository;
    private final ChatMessageReaderMapper chatMessageReaderMapper;

    public ChatRoom saveRoom(final ChatRoom chatRoom) {
        final User currentUser = securityManagementService.findCurrentUser();
        chatRoomValidations(chatRoom, currentUser);
        return chatRoomService.save(buildChatRoom(chatRoom, currentUser)).withCreationUser(currentUser);
    }

    public ChatRoom buildChatRoom(final ChatRoom chatRoom, final User currentUser) {
        final List<User> currentUserAsList = List.of(currentUser);
        final LocalDateTime now = LocalDateTime.now();
        chatRoomValidations(chatRoom);
        Random random = new Random();
        final ChatRoom fromDDBB = Objects.nonNull(chatRoom.getId())
                ? chatRoomService.findById(chatRoom.getId())
                .withName(StringUtils.isBlank(chatRoom.getName())
                        ? CHAT_DEFAULT_NAME_PREFIX + now.getNano()
                        : chatRoom.getName()
                )
                .withCustomer(Objects.nonNull(chatRoom.getCustomer()) ? Customer.builder().id(chatRoom.getCustomer().getId()).build() : null)
                .withGroupAccount(!Objects.isNull(chatRoom.getGroupAccount()) ? chatRoom.getGroupAccount() : null)
                .withMembers(
                        CollectionUtils.isEmpty(chatRoom.getMembers())
                                ? currentUserAsList
                                : Stream.concat(chatRoom.getMembers().stream(), currentUserAsList.stream()).toList())

                : chatRoomService.save(chatRoom.withCreationUser(
                Objects.isNull(chatRoom.getId())
                        ? currentUser
                        : currentUser
        ));
        return fromDDBB
                .withName(
                        StringUtils.isBlank(chatRoom.getName())
                                ? CHAT_DEFAULT_NAME_PREFIX + now.getNano()
                                : chatRoom.getName()
                )
                .withCustomer(Objects.nonNull(chatRoom.getCustomer()) ? Customer.builder().id(chatRoom.getCustomer().getId()).build() : null)
                .withGroupAccount(!Objects.isNull(chatRoom.getGroupAccount()) ? chatRoom.getGroupAccount() : null)
                .withMembers(
                        CollectionUtils.isEmpty(chatRoom.getMembers())
                                ? currentUserAsList
                                : Stream.concat(chatRoom.getMembers().stream(), currentUserAsList.stream()).toList())
                .withCreationUser(
                        Objects.isNull(chatRoom.getId())
                                ? securityManagementService.findCurrentUser()
                                : fromDDBB.getCreationUser()
                )
                .withCreationTime(
                        Objects.isNull(chatRoom.getId())
                                ? LocalDateTime.now().withHour(now.getHour() + 2).withNano(random.nextInt(999999999 + 1))
                                : fromDDBB.getCreationTime()
                );
    }

    private void chatRoomValidations(ChatRoom chatRoom) {
        if (Objects.nonNull(chatRoom.getId())) {
            ChatRoom originalChatRoom = chatRoomService.findById(chatRoom.getId());
            List<User> currentCustomer = new ArrayList<>();
            List<User> originalCustomers = new ArrayList<>();
            if (!CollectionUtils.isEmpty(originalChatRoom.getMembers())) {
                originalCustomers = originalChatRoom.getMembers().stream()
                        .filter(member -> member.getTypeUser().equals(UserTypeEnum.CUSTOMER.name()) ||
                                member.getTypeUser().equals(UserTypeEnum.ASSOCIATE.name()))
                        .collect(Collectors.toList());
            }

            if(!CollectionUtils.isEmpty(  chatRoom.getMembers())){
                chatRoom.getMembers().stream()
                        .forEach(users -> currentCustomer.add(userService.findById(users.getId())));
            }

            if (!originalCustomers.stream()
                    .anyMatch(e -> currentCustomer.stream()
                            .anyMatch(user -> user.getId().intValue() == e.getId().intValue()))) {
                throw new BusinessRuleException("CUSTOMER type members cannot be modified.");
            }
        }
    }

    public List<ChatRoom> findAllChatRoomMember(final Integer idCustomer) {
        return chatRoomService.findAllChatRoomCustomer(idCustomer);
    }

    public Long findAllChatRoomAssociate(final Integer idCustomer) {
        return chatRoomService.findAllChatRoomAssociate(idCustomer);
    }

    public ChatOutOffice saveOutOffice(final ChatOutOffice outOffice) {
        validations(outOffice);
        return chatMessageOutOfficeService.save(buildChatOutOffice(outOffice)
                .withStatus(DeletedStatus.builder().id(DeletedStatusEnum.ACTIVE.getId())
                        .name("Active").build()).withName(outOffice.getName()));
    }

    public List<ChatOutOffice> findAllOutOffice(final ChatOutOfficeSpecification specification,
                                                final Integer page, final Integer size, final String direction,
                                                final String attribute) {
        validatePermissionUser();
        final Direction directionEnum =
                Arrays.stream(Direction.values()).anyMatch(v -> v.name().equalsIgnoreCase(direction))
                        ? Direction.fromString(direction)
                        : Direction.DESC;
        final Sort sort = Sort.by(directionEnum, attribute);
        final Pageable pageable = PageRequest.of(page, size, sort);
        return chatMessageOutOfficeService.findAllOutOffice(specification, pageable);
    }

    public Long count(final ChatOutOfficeSpecification specification) {
        return chatMessageOutOfficeService
                .count(addSoftDeleteChatOutOffice(1, specification));
    }

    public void deleteById(final Integer id) {
        validatePermissionUser();
        chatMessageOutOfficeService.deleteById(id);
    }

    public void deleteChatMessageById(final Integer id) {
        chatMessageService.existsById(id);
        chatMessageService.deteleById(id);
    }


    private ChatOutOffice buildChatOutOffice(final ChatOutOffice outOffice) {
        final ChatOutOffice chatOutOfficeFromDDBB = Objects.nonNull(outOffice.getId())
                ? chatMessageOutOfficeService
                .findById(outOffice.getId())
                .withModificationUser(securityManagementService.findCurrentUser())
                .withStatus(outOffice.getStatus())
                .withValue(outOffice.getValue())
                .withDays(outOffice.getDays())
                .withStart(outOffice.getStart())
                .withName(outOffice.getName())
                .withEnd(outOffice.getEnd()) : chatMessageOutOfficeService.save(outOffice);
        return chatOutOfficeFromDDBB
                .withStatus(outOffice.getStatus())
                .withValue(outOffice.getValue())
                .withDays(outOffice.getDays())
                .withStart(outOffice.getStart())
                .withName(outOffice.getName())
                .withCreationUser(securityManagementService.findCurrentUser())
                .withEnd(outOffice.getEnd());
    }

    private void validations(final ChatOutOffice outOffice) {
        validateValue(outOffice);
        validateDate(outOffice);
        validatePermissionUser();
    }

    private void validatePermissionUser() {
        final User currentUser = securityManagementService.findCurrentUser();
        if (!hasCreationPermissionOutOffice(currentUser)) {
            throw new BusinessRuleException("Cannot create Out Office");
        }
    }

    private void validateValue(ChatOutOffice outOffice) {
        if (StringUtils.isBlank(outOffice.getValue())) {
            throw new BusinessRuleException(CommonConstants.THIS_FIELD_IS_MANDATORY_MESSAGE);
        }
    }

    private void validateDate(ChatOutOffice outOffice) {
        if (outOffice.getStart().equals(outOffice.getEnd())) {
            throw new BusinessRuleException("the date cannot be the same");
        }

        if (StringUtils.isBlank(outOffice.getStart()) || StringUtils.isBlank(outOffice.getEnd())) {
            throw new BusinessRuleException("the date cannot be the same");
        }
    }

    @Transactional
    public List<ChatRoom> findAll(final ChatRoomv1Specification specification, final Integer page,
                                  final Integer size, final String direction,
                                  final String attribute) {
        final User currentUser = securityManagementService.findCurrentUser();
        final Direction directionEnum =
                Arrays.stream(Direction.values()).anyMatch(v -> v.name().equalsIgnoreCase(direction))
                        ? Direction.fromString(direction)
                        : Direction.ASC;
        final Sort sort = Sort.by(directionEnum, attribute);
        final Pageable pageable = PageRequest.of(page, size, sort);

        return verifyTypeUserAndPermission(filterMessage(specification, currentUser, pageable)
                .stream()
                .collect(Collectors.toList()), currentUser);
    }

    @Transactional
    public List<ChatRoom> findAllChatArchive(final ChatRoomv2Specification specification, final Integer page,
                                             final Integer size, final String direction,
                                             final String attribute) {
        final User currentUser = securityManagementService.findCurrentUser();
        final Direction directionEnum =
                Arrays.stream(Direction.values()).anyMatch(v -> v.name().equalsIgnoreCase(direction))
                        ? Direction.fromString(direction)
                        : Direction.ASC;
        final Sort sort = Sort.by(directionEnum, attribute);
        final Pageable pageable = PageRequest.of(page, size, sort);
        hasAccessToArchiveChat(currentUser);
        return chatRoomService.findAllChatArchive(specification, currentUser.getId(), isInternalUser(currentUser), pageable).stream()
                .map(chatRoom -> chatRoom
                        .withLastMessage(setRead(chatRoom, currentUser, chatMessageService.findLastMessageByChatRoom(chatRoom.getId())))
                        .withTotalUnreadMessages(chatMessageService.countUnreadMessagesByChatRoom(chatRoom.getId(), currentUser.getId()))
                ).collect(Collectors.toList())
                .stream()
                .collect(Collectors.toList());
    }

    public List<ChatRoom> verifyTypeUserAndPermission(List<ChatRoom> rooms, User currentUser) {
        List<ChatRoom> chatRooms = new ArrayList<>();
        rooms.stream().forEach(room -> {
            if (room.getType().equals(ChatRoomTypeEnum.INTERNAL)) {
                if (currentUser.getTypeUser().toUpperCase().equals(UserTypeEnum.CRM.name())) {
                    if(!CollectionUtils.isEmpty(room.getMembers())){
                        if (room.getMembers().stream()
                                .anyMatch(user -> user.getId().intValue() == currentUser.getId().intValue())) {
                            chatRooms.add(room);
                        }
                    }
                } else {
                    if (room.getMembers().stream()
                            .anyMatch(user -> user.getId().intValue() == currentUser.getId().intValue())) {
                        chatRooms.add(room);
                    }
                }
            } else {
                if (room.getType().equals(ChatRoomTypeEnum.CUSTOMER)
                        || room.getType().equals(ChatRoomTypeEnum.GROUPACCOUNT)) {
                    if (currentUser.getTypeUser().toUpperCase().equals(UserTypeEnum.CRM.name())) {
                        chatRooms.add(room);
                    } else {
                        if (room.getMembers().stream()
                                .anyMatch(user -> user.getId().intValue() == currentUser.getId().intValue())) {
                            chatRooms.add(room);
                        }
                    }
                }
            }
        });
        return chatRooms;
    }

    private List<ChatRoom> filterMessage(ChatRoomv1Specification specification, User currentUser, Pageable pageable) {
        List<ChatRoom> rooms = new ArrayList<>();
        if (Objects.nonNull(specification.getUnRead()) && specification.getUnRead()) {


            rooms = chatRoomService.findAll(specification, currentUser.getId(), isInternalUser(currentUser), pageable)
                    .stream()
                    .map(chatRoom -> chatRoom
                            .withLastMessage(setRead(chatRoom, currentUser, chatMessageService
                                    .findLastMessageByChatRoom(chatRoom.getId())))
                            .withTotalUnreadMessages(chatMessageService
                                    .countUnreadMessagesByChatRoom(chatRoom.getId(), currentUser.getId()))
                    )
                    .collect(Collectors.toList()).stream()
                    .filter(chatRoom -> chatRoom.getTotalUnreadMessages() > 0 && chatRoom.getArchive() == Boolean.FALSE)
                    .sorted(Comparator.comparing(chatRoom -> Objects.isNull(chatRoom.getLastMessage())
                                    ? chatRoom.getCreationTime() : chatRoom.getLastMessage().getCreationTime(),
                            Comparator.reverseOrder()))
                    .collect(Collectors.toList());

        } else if (Objects.nonNull(specification.getUnRead()) && !specification.getUnRead()) {
            rooms = chatRoomService.findAll(specification, currentUser.getId(),
                            isInternalUser(currentUser), pageable).stream()
                    .map(chatRoom -> chatRoom
                            .withLastMessage(setRead(chatRoom, currentUser, chatMessageService
                                    .findLastMessageByChatRoom(chatRoom.getId())))
                            .withTotalUnreadMessages(chatMessageService
                                    .countUnreadMessagesByChatRoom(chatRoom.getId(), currentUser.getId()))
                    )
                    .collect(Collectors.toList()).stream()
                    .filter(chatRoom -> chatRoom.getTotalUnreadMessages() == 0 && chatRoom.getArchive() == Boolean.FALSE)
                    .sorted(Comparator.comparing(chatRoom -> Objects.isNull(chatRoom.getLastMessage())
                                    ? chatRoom.getCreationTime() : chatRoom.getLastMessage().getCreationTime(),
                            Comparator.reverseOrder()))
                    .collect(Collectors.toList());
        }
        return rooms;
    }

    public List<ChatMessage> saveMessage(final Integer chatId, final ChatMessage chatMessage) throws ParseException {
        ChatMessage messageOffice = null;
        List<ChatMessage> chatMessages = new ArrayList<>();
        final ChatRoom chatRoom = chatRoomService.findById(chatId);
        final User currentUser = securityManagementService.findCurrentUser();
        hasAccessToChat(chatRoom, currentUser);
        final String resultOutOfficeValue = validateRangeDate();

        chatMessages.add(chatMessageService.save(buildChatMessage(chatRoom, chatMessage, currentUser)));
        if (StringUtils.isNotBlank(resultOutOfficeValue)) {
            messageOffice = chatMessageService.save(buildChatMessageOutOffice(chatRoom, chatMessage));
            chatMessages.add(messageOffice);
        }
        return chatMessages;
    }

    public ChatMessage saveFile(final Integer chatId, final MultipartFile file, final String extension,
                                final String text,
                                final String value) {
        final ChatRoom chatRoom = chatRoomService.findById(chatId);
        final User currentUser = securityManagementService.findCurrentUser();
        final String extensionFile = FilenameUtils.getExtension(file.getOriginalFilename());
        hasAccessToChatFile(chatRoom, currentUser);
        validateExtensionFile(extension, extensionFile);
        File messageFile = getFileMessage(file, extension, text, currentUser, chatRoom);
        return getChatMessage(chatRoom, messageFile, value);
    }

    public static String extractFileName(String path) {
        // Buscar la última aparición de '/' o '\' en la cadena
        int lastSlashIndex = Math.max(path.lastIndexOf('/'), path.lastIndexOf('\\'));

        // Verificar si se encontró un separador
        if (lastSlashIndex >= 0) {
            // Extraer el nombre del archivo a partir de la última aparición del separador
            return path.substring(lastSlashIndex + 1);
        } else {
            // Si no se encontró un separador, simplemente devolver la cadena original
            return path;
        }
    }

    private File getFileMessage(MultipartFile file, String extension, final String text, User currentUser, ChatRoom chatRoom) {
        File messageFile = null;
        Customer customer = null;
        if (Objects.nonNull(file)) {
            if (Objects.nonNull(chatRoom.getCustomer()) && !chatRoom.getType().equals(ChatRoomTypeEnum.INTERNAL)) {
                customer = customerService.findById(chatRoom.getCustomer().getId());
            }
            int lastIndex = file.getOriginalFilename().lastIndexOf(".pdf");
            String urlFile = azureFileService.uploadFile(file, AZURE_PATH, extension);
            messageFile = fileService.save(
                    File.builder()
                            .url(urlFile)
                            .extension(extension)
                            .text(text)
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

    private ChatMessage getChatMessage(ChatRoom chatRoom, File messageFile, final String value) {
        final LocalDateTime now = LocalDateTime.now();
        Random random = new Random();
        ChatMessage message = chatMessageService.save(
                ChatMessage.builder()
                        .value(Objects.nonNull(value) ? value : null)
                        .type(ChatMessageTypeEnum.FILE)
                        .chatRoom(chatRoom)
                        .files(List.of(fileMapper.modelToResponse(messageFile)))
                        .sender(securityManagementService.findCurrentUser())
                        .creationTime(LocalDateTime.now().withSecond(now.getSecond() + 2).withNano(random.nextInt(999999999 + 1)))
                        .build());
        return message;
    }

    /**
     * if (chatMessage.getSender().getId().intValue() == currentUser.getId()) {
     * throw new BusinessRuleException("This user was the sender. Cannot read it.");
     * }
     * <p>
     * if (!CollectionUtils.isEmpty(chatMessage.getReaders())
     * && chatMessage.getReaders().stream().anyMatch(r -> r.getReader().getId().intValue() == currentUser.getId())) {
     * throw new BusinessRuleException("This message has already been read");
     * }
     *
     * @param chatId
     */

    @Transactional
    public void readMessage(final Integer chatId) {
        final ChatRoom chatRoom = chatRoomService.findById(chatId);
        final User currentUser = securityManagementService.findCurrentUser();
        hasAccessToChat(chatRoom, currentUser);

        final List<ChatMessage> chatMessages = chatMessageService.findAllMessageChatRoom(chatRoom);
        if (!CollectionUtils.isEmpty(chatMessages)) {
            chatMessages.forEach(chatMessage -> {//TODO: elemento de la lista de mensajes

                if (CollectionUtils.isEmpty(chatMessage.getReaders())) { // TODO : Sin mensajes leidos ( 152)
                    chatMessageReaderService.save(ChatMessageReader
                            .builder()
                            .message(chatMessage)
                            .readingTime(LocalDateTime.now())
                            .reader(currentUser)
                            .build());
                } else {
                    chatMessage.getReaders().forEach(
                            chatMessageReader -> {
                                if (!(chatMessageReader.getReader().getId().intValue() == currentUser.getId())) {
                                    chatMessageReaderService.save(ChatMessageReader
                                            .builder()
                                            .message(chatMessage)
                                            .readingTime(LocalDateTime.now())
                                            .reader(currentUser)
                                            .build());
                                }
                            }
                    );
                }
            });
        }
    }

    public void archiveMessage(final Integer chatId) {
        chatRoomService.existsById(chatId);
        ChatRoom chatRoom = chatRoomService.findById(chatId);
        final User currentUser = securityManagementService.findCurrentUser();
        validation(currentUser, chatRoom);
        chatRoomService.updateArchiveChat(chatId);
    }

    private void validation(User currentUser, ChatRoom chatRoom) {
        hasAccessToArchiveChat(currentUser);
        validateTypeChatAndMemberStatus(currentUser, chatRoom);
    }

    private static void hasAccessToArchiveChat(User currentUser) {
        if (isInternalUserChatArchive(currentUser)) {
            return;
        }
        throw new BusinessRuleException("This user not have access right to archive this chat.");
    }

    private void validateTypeChatAndMemberStatus(User currentUser, ChatRoom chatRoom) {
        AtomicBoolean result = new AtomicBoolean(false);
        AtomicReference<User> users = new AtomicReference<User>();
        if (isInternalUserChatArchive(currentUser)
                && chatRoom.getType().equals(ChatRoomTypeEnum.CUSTOMER)
                || chatRoom.getType().equals(ChatRoomTypeEnum.GROUPACCOUNT)) {

            chatRoom.getMembers().forEach(user -> {
                users.set(userService.findById(user.getId()));
                if (users.get().getTypeUser().equals(UserTypeEnum.CUSTOMER.name())
                        && users.get().getStatus().getId().intValue() == DeletedStatusEnum.DELETED.getId()) {
                    result.set(true);
                }
            });

            if (result.get()) {
                return;
            }
        }
        throw new BusinessRuleException("The chat cannot be archived since the customer is still active.");
    }

    public long countChatRoom(final ChatRoomv1Specification specification) {
        return chatRoomService.countChatRoom(specification);
    }

    public long countChatRoomArchive(final ChatRoomv2Specification specification) {
        return chatRoomService.countChatRoomArchive(specification);
    }

    public long countChatRoomTypeCustomer(final ChatRoomTypeSpecification specification) {
        return chatRoomService.countChatRoomTypeCustomer(specification);
    }

    public long countMessages(final Integer chatId, final ChatMessageSpecification specification) {
        return chatMessageService.countMessages(chatId, specification);
    }

    public long countChatMessages(final ChatMessageSpecification specification) {
        return chatMessageService.countChatMessages(specification);
    }

    @Transactional
    public List<ChatMessage> findAllMessages(final Integer chatId,
                                             final ChatMessageSpecification specification, final Integer page, final Integer size, final String direction,
                                             final String attribute) {
        final ChatRoom chatRoom = chatRoomService.findById(chatId);
        final User currentUser = securityManagementService.findCurrentUser();
        hasAccessToChat(chatRoom, currentUser);

        final Direction directionEnum =
                Arrays.stream(Direction.values()).anyMatch(v -> v.name().equalsIgnoreCase(direction))
                        ? Direction.fromString(direction)
                        : Direction.DESC;
        final Sort sort = Sort.by(directionEnum, attribute)
                .and(Sort.by(Direction.DESC, "id"));
        final Pageable pageable = PageRequest.of(page, size, sort);
        return setRead(chatRoom, currentUser, chatMessageService.findAll(chatId, specification, pageable)
                .stream()
                .map(chatMessage -> chatMessage.withFiles(fileMapper.modelsToResponseList(fileService.findAllChatMessageFile(chatMessage.getId()))))
                .collect(Collectors.toList()));
    }

    @Transactional
    public List<ChatMessage> findAllChatMessages(final ChatMessageSpecification specification, final Integer page, final Integer size, final String direction,
                                                 final String attribute, final String value, final String advanceSearch) {
        final User currentUser = securityManagementService.findCurrentUser();
        final Direction directionEnum =
                Arrays.stream(Direction.values()).anyMatch(v -> v.name().equalsIgnoreCase(direction))
                        ? Direction.fromString(direction)
                        : Direction.DESC;
        final Sort sort = Sort.by(directionEnum, attribute).and(Sort.by(Direction.DESC, "id"));
        final Pageable pageable = PageRequest.of(page, size, sort);
        return chatMessageService.findAllMessages(specification, pageable, value, advanceSearch)
                .stream()
                .map(chatMessage -> chatMessage.withFiles(fileMapper.modelsToResponseList(fileService.findAllChatMessageFile(chatMessage.getId()))))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ChatMessage> findUnread(final Integer chatId, final Integer page, final Integer size) {
        final ChatRoom chatRoom = chatRoomService.findById(chatId);
        final User currentUser = securityManagementService.findCurrentUser();
        hasAccessToChat(chatRoom, currentUser);

        final Pageable pageable = PageRequest.of(page, size, DEFAULT_MESSAGE_SORT);
        return chatMessageService.findUnread(chatId, currentUser.getId(), pageable)
                .stream()
                .map(chatMessage -> chatMessage.withFiles(fileMapper.modelsToResponseList(fileService.findAllChatMessageFile(chatMessage.getId()))))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ChatMessage> findLast(final Integer chatId, final Integer messageId, final Integer page,
                                      final Integer size) {
        final ChatRoom chatRoom = chatRoomService.findById(chatId);
        final User currentUser = securityManagementService.findCurrentUser();
        hasAccessToChat(chatRoom, currentUser);

        final Pageable pageable = PageRequest.of(page, size, DEFAULT_MESSAGE_SORT);
        return setRead(chatRoom, currentUser, chatMessageService.findLast(chatId, messageId, pageable))
                .stream().map(chatMessage -> chatMessage
                        .withFiles(fileMapper.modelsToResponseList(fileService.findAllChatMessageFile(chatMessage.getId()))))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ChatMessage> findNext(final Integer chatId, final Integer messageId, final Integer page,
                                      final Integer size) {
        final ChatRoom chatRoom = chatRoomService.findById(chatId);
        final User currentUser = securityManagementService.findCurrentUser();
        hasAccessToChat(chatRoom, currentUser);
        final Pageable pageable = PageRequest.of(page, size, DEFAULT_MESSAGE_ASC_SORT);
        return setRead(chatRoom, currentUser, chatMessageService.findNext(chatId, messageId, pageable)
                .stream().map(chatMessage -> chatMessage
                        .withFiles(fileMapper.modelsToResponseList(fileService.findAllChatMessageFile(chatMessage.getId()))))
                .collect(Collectors.toList()));
    }

    public boolean findLastMessage() {
        ChatMessage message = chatMessageService.findLastMessage();
        Duration duration = Duration.between(message.getCreationTime(), LocalDateTime.now());
        return isResultLastMessage(duration.getSeconds());
    }

    private static boolean isResultLastMessage(long second) {
        boolean result = false;
        if (second < 10) {
            result = true;
        } else if (second >= 10) {
            result = false;
        }
        return result;
    }

    private void chatRoomValidations(final ChatRoom chatRoom, final User currentUser) {
        validatePermissions(chatRoom, currentUser);
        validateMembers(chatRoom, currentUser);
    }

    private void validatePermissions(final ChatRoom chatRoom, final User currentUser) {
        if (ChatRoomTypeEnum.INTERNAL == chatRoom.getType() && !isInternalUser(currentUser)) {
            throw new BusinessRuleException("Cannot create a INTERNAL chat");
        }

        if (!hasCreationPermission(currentUser) && currentUser.getTypeUser().equals(UserTypeEnum.CRM.name())) {
            throw new BusinessRuleException("Cannot create any chat");
        }
    }

    private void validateMembers(final ChatRoom chatRoom, final User currentUser) {
        if (CollectionUtils.isEmpty(chatRoom.getMembers())
                && ChatRoomTypeEnum.INTERNAL == chatRoom.getType()) {
            throw new BusinessRuleException("By Internal Chats, the members list is mandatory");
        }

        if (!CollectionUtils.isEmpty(chatRoom.getMembers())) {
            chatRoom.getMembers()
                    .forEach(
                            member -> {
                                if (Objects.isNull(member.getId())) {
                                    throw new BusinessRuleException("In members field, the id are mandatory");
                                }
                            }
                    );
            chatRoom.getMembers()
                    .forEach(
                            member -> userService.existsById(member.getId())
                    );
            chatRoom.getMembers()
                    .forEach(
                            member -> {
                                if (member.getId().intValue() == currentUser.getId()) {
                                    /*throw new BusinessRuleException(
                                            "Can't to add the creator in the chat members list. There's no need.");*/
                                    log.info("Can't to add the creator in the chat members list. There's no need.");
                                }
                            }
                    );//TODO:CONVERSARLO CON MARCOS

            if (ChatRoomTypeEnum.INTERNAL == chatRoom.getType()) {
                chatRoom.getMembers()
                        .forEach(
                                member -> {
                                    final User byId = userService.findById(member.getId());
                                    long countChat = chatRoomService.findAllChatRoomCustomerChat(byId.getId());
                                    if (!isInternalUser(byId)) {
                                        throw new BusinessRuleException("Only internal users can be added");
                                    }

                                    if (isInternalUser(byId) &&
                                            ChatRoomTypeEnum.INTERNAL == chatRoom.getType()
                                            && countChat > 0) {
                                        throw new BusinessRuleException("Only internal users can be added");
                                    }
                                }
                        );
            } else if (ChatRoomTypeEnum.CUSTOMER == chatRoom.getType()) {
                final AtomicInteger customerUserCounter = new AtomicInteger(0);
                chatRoom.getMembers()
                        .forEach(
                                member -> {
                                    final User byId = userService.findById(member.getId());
                                    if (isInternalUser(byId)) {
                                        throw new BusinessRuleException("An internal user cannot be added as a member in an Customer Chat");
                                    }
                                    /*if (isCustomerUser(byId) && isCustomerUser(currentUser)) {//TODO:Verificarla
                                        throw new BusinessRuleException(
                                                "Other customer user cannot be added as a member in an Customer Chat");
                                    }*/
                                    if (isCustomerUser(byId)) {
                                        customerUserCounter.incrementAndGet();
                                    }
                                }
                        );

                if (customerUserCounter.get() > 1) {
                    throw new BusinessRuleException("It can only contain one Customer per chat.");
                }
            }
        }
    }

    private String validateRangeDate() {
        DateFormat horaFormat = new SimpleDateFormat("HH:mm:ss");
        TimeZone tz = TimeZone.getDefault();
        horaFormat.setTimeZone(tz);   // This line converts the given date into UTC time zone
        User user = securityManagementService.findCurrentUser();
        return getResultValue(user, horaFormat);
    }

    private String getResultValue(User user, DateFormat horaFormat) {
        String resultValue = null;
        if (user.getTypeUser().equals(UserTypeEnum.CUSTOMER.name())
                || user.getTypeUser().equals(UserTypeEnum.ASSOCIATE.name())) {
            Optional<ChatOutOffice> chatOutOffice = chatMessageOutOfficeService.findAll().stream()
                    .filter(e -> {
                        try {
                            return (horaFormat.format(horaFormat.parse(e.getStart())).compareTo(horaFormat.format(new Date())) <= 0)
                                    && (horaFormat.format(horaFormat.parse(e.getEnd())).compareTo(horaFormat.format(new Date())) >= 0)
                                    && (e.getDays().stream()
                                    .anyMatch(dayOffice -> dayOffice.getId().equals(LocalDateTime.now().getDayOfWeek().getValue())));
                        } catch (ParseException ex) {
                            throw new BusinessRuleException("Date Format incorrect");
                        }
                    })
                    .findAny();

            if (chatOutOffice.isPresent()) {
                log.info("You are in the range of out office: " + horaFormat.format(new Date()));
                resultValue = chatOutOffice.get().getValue();
            }
        }
        return resultValue;
    }

    private ChatMessage buildChatMessage(final ChatRoom chatRoom, final ChatMessage chatMessage,
                                         final User currentUser) {
        Random random = new Random();
        return chatMessage
                .withChatRoom(chatRoom)
                .withType(ChatMessageTypeEnum.PLAIN_TEXT)
                .withCreationTime(LocalDateTime.now().withNano(random.nextInt(999999999 + 1)))
                .withSender(currentUser);
    }

    private ChatMessage buildChatMessageOutOffice(final ChatRoom chatRoom, final ChatMessage chatMessage) throws ParseException {
        log.info("Out Office for  ChatRoom ID :" + chatRoom.getId());
        Random random = new Random();
        return chatMessage
                .withChatRoom(chatRoom)
                .withType(ChatMessageTypeEnum.SYSTEM)
                .withCreationTime(LocalDateTime.now().withSecond(LocalDateTime.now().getSecond() + 1)
                        .withNano(random.nextInt(999999999 + 1)))
                .withValue(validateRangeDate())
                .withSender(User.builder().id(40).build());
    }

    private static boolean hasCreationPermission(final User currentUser) {
        return currentUser
                .getRoles()
                .stream()
                .flatMap(r -> r.getPermissions().stream())
                .anyMatch(p -> CREATION_PERMISSION.equalsIgnoreCase(p.getName()));
    }

    private static boolean hasCreationPermissionOutOffice(final User currentUser) {
        return currentUser
                .getRoles()
                .stream()
                .flatMap(r -> r.getPermissions().stream())
                .anyMatch(p -> CREATION_PERMISSION_OUTOFFICE.equalsIgnoreCase(p.getName()));
    }

    private static boolean isInternalUser(final User currentUser) {
        return currentUser
                .getRoles()
                .stream()
                .flatMap(r -> r.getPermissions().stream())
                .anyMatch(p -> INTERNAL_PERMISSION.equalsIgnoreCase(p.getName()));
    }

    private static boolean isInternalUserChatArchive(final User currentUser) {
        return currentUser
                .getRoles()
                .stream()
                .flatMap(r -> r.getPermissions().stream())
                .anyMatch(p -> CHAT_ARCHIVE.equalsIgnoreCase(p.getName()));
    }

    private static boolean isCustomerUser(final User currentUser) {
        return currentUser
                .getRoles()
                .stream()
                .flatMap(r -> r.getPermissions().stream())
                .anyMatch(p -> CUSTOMER_PERMISSION.equalsIgnoreCase(p.getName()));
    }

    private static boolean isUserCrmPermissionFile(final User currentUser) {
        return currentUser
                .getRoles()
                .stream()
                .flatMap(r -> r.getPermissions().stream())
                .anyMatch(p -> CHAT_FILE.equalsIgnoreCase(p.getName()));
    }

    private void hasAccessToChatFile(final ChatRoom chatRoom, final User currentUser) {
        if (isUserCrmPermissionFile(currentUser)
                && currentUser.getTypeUser().toUpperCase().equals(UserTypeEnum.CRM.name())
                && chatRoom.getType().equals(ChatRoomTypeEnum.INTERNAL)) {
            return;
        } else if (isUserCrmPermissionFile(currentUser) &&
                currentUser.getTypeUser().toUpperCase().equals(UserTypeEnum.CRM.name())
                && chatRoom.getType().equals(ChatRoomTypeEnum.CUSTOMER)
                || chatRoom.getType().equals(ChatRoomTypeEnum.GROUPACCOUNT)) {
            return;
        }
        if (isUserCrmPermissionFile(currentUser)) {
            return;
        }
        throw new BusinessRuleException("the current user does not have the right to send files through the chat.");
    }

    private void hasAccessToChat(final ChatRoom chatRoom, final User currentUser) {
        if (isChatMember(chatRoom, currentUser)) {
            return;
        }

        if (ChatRoomTypeEnum.CUSTOMER == chatRoom.getType()
                || ChatRoomTypeEnum.GROUPACCOUNT == chatRoom.getType() && isInternalUser(currentUser)) {

            if (chatRoom.getMembers().stream().anyMatch(user -> user.getId().intValue() != currentUser.getId().intValue())) {
                ChatRoom room = getBuildChatRoom(chatRoom, currentUser, List.of(currentUser));
                saveRoom(room);
            }
            return;
        }

        if (isChatMember(chatRoom, currentUser)
                && currentUser.getTypeUser().toUpperCase().equals(UserTypeEnum.CRM.name())) {
            if (chatRoom.getMembers().stream().anyMatch(user -> user.getId().intValue() != currentUser.getId().intValue())) {
                ChatRoom room = getBuildChatRoom(chatRoom, currentUser, List.of(currentUser));
                saveRoom(room);
            }
            return;
        }

        if (isChatMember(chatRoom, currentUser) &&
                currentUser.getTypeUser().toUpperCase().equals(UserTypeEnum.SYSTEM.name())) {
            return;
        }
        throw new BusinessRuleException("This user not have access to this chat.");
    }

    private static ChatRoom getBuildChatRoom(ChatRoom chatRoom, User currentUser, List<User> currentUserAsList) {
        return ChatRoom.builder()
                .id(chatRoom.getId())
                .name(chatRoom.getName())
                .members(chatRoom.getMembers())
                .creationTime(chatRoom.getCreationTime())
                .groupAccount(chatRoom.getGroupAccount())
                .id(chatRoom.getId())
                .creationUser(currentUser.getCreationUser())
                .customer(chatRoom.getCustomer())
                .lastMessage(chatRoom.getLastMessage())
                .build();
    }

    private static boolean isChatMember(final ChatRoom chatRoom, final User currentUser) {
        return !CollectionUtils.isEmpty(chatRoom.getMembers())
                && chatRoom.getMembers().stream().anyMatch(m -> m.getId().intValue() == currentUser.getId());
    }

    private List<ChatMessage> setRead(final ChatRoom chatRoom, final User currentUser,
                                      final List<ChatMessage> input) {
        return CollectionUtils.isEmpty(input)
                ? input
                : input.stream().map(message -> setRead(chatRoom, currentUser, message)).toList();
    }

    private ChatMessage setRead(final ChatRoom chatRoom, final User currentUser, final ChatMessage message) {
        if (Objects.nonNull(message)) {
            if (currentUser.getId().intValue() == message.getSender().getId()) {
                return message.withRead(Boolean.TRUE).withReadingTime(message.getCreationTime());
            } else if (!CollectionUtils.isEmpty(message.getReaders())) {
                final Optional<ChatMessageReader> readerOpt = message.getReaders()
                        .stream()
                        .filter(r -> currentUser.getId().intValue() == r.getReader().getId()
                                || (
                                ChatRoomTypeEnum.CUSTOMER == chatRoom.getType()
                                        && isInternalUser(currentUser)
                                        && isInternalUser(userService.findById(r.getReader().getId())))).findAny();
                if (readerOpt.isPresent()) {
                    return message.withRead(Boolean.TRUE).withReadingTime(readerOpt.get().getReadingTime());
                }
            }
        }
        return message;
    }
}
