package com.martzatech.vdhg.crmprojectback.application.services;

import com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants;
import com.martzatech.vdhg.crmprojectback.application.enums.DeletedStatusEnum;
import com.martzatech.vdhg.crmprojectback.application.exceptions.BusinessRuleException;
import com.martzatech.vdhg.crmprojectback.application.exceptions.FieldIsMissingException;
import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.StatusNotFoundException;
import com.martzatech.vdhg.crmprojectback.domains.chat.enums.ChatMessageTypeEnum;
import com.martzatech.vdhg.crmprojectback.domains.chat.mappers.FileMapper;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.ChatMessage;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.ChatRoom;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.File;
import com.martzatech.vdhg.crmprojectback.domains.chat.services.ChatMessageService;
import com.martzatech.vdhg.crmprojectback.domains.chat.services.ChatRoomService;
import com.martzatech.vdhg.crmprojectback.domains.chat.services.FileService;
import com.martzatech.vdhg.crmprojectback.domains.customers.enums.NoteTypeEnum;
import com.martzatech.vdhg.crmprojectback.domains.customers.enums.OfferGLobalStatusEnum;
import com.martzatech.vdhg.crmprojectback.domains.customers.enums.OfferStatusEnum;
import com.martzatech.vdhg.crmprojectback.domains.customers.enums.OrderStatusEnum;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Customer;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Note;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Offer;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Order;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.NoteService;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.OfferService;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.OrderService;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.OrderSpecification;
import com.martzatech.vdhg.crmprojectback.domains.security.models.DeletedStatus;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import com.martzatech.vdhg.crmprojectback.domains.security.services.UserService;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.FileApiMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
@Service
public class OrderManagementService {
    public static final int ID_USER_SYSTEM = 2;
    private final OrderService orderService;
    private final OfferService offerService;
    private final SecurityManagementService securityManagementService;
    private final ReportPdfService reportService;
    private final NoteService noteService;
    private final FileService fileService;
    private ChatRoomService roomService;
    private UserService userService;
    private ChatMessageService messageService;
    private FileApiMapper fileApiMapper;
    private FileMapper fileMapper;

    @Transactional
    public Order save(final Order order) throws IOException {
        validations(order);
        acceptAllOffers(order);
        final Offer offer = offerService.findById(order.getOffer().getId());
        final String reportUrl = reportService.exportOrderPdf(order, offer);
        final String reportUrlInternal = reportService.exportOrderPdfInternal(order, offer);

        Customer customer = offer.getCustomer();
        User member = userService.findByIdCustomer(customer.getId());
        ChatRoom room = validateChatRoom(member);
        buildMessageFileChatOrder(room, member, offer, reportUrl);

        return orderService.save(build(order)
                .withPdfUrl(reportUrl)
                .withPdfUrlInternal(reportUrlInternal));
    }

    @Transactional
    public boolean sendEmailAndChat(final Integer orderId) {
        Order order = orderService.findById(orderId);
        boolean resultBuilt = false;
        Offer offer = order.getOffer();
        Customer customer = Objects.nonNull(offer.getCustomer())? offer.getCustomer():null;
        if(Objects.nonNull(customer)){
            log.info("Customer Send Order ID:{}",customer.getId());
            User member = userService.findByIdCustomer(customer.getId());
            ChatRoom room = validateChatRoom(member);
            resultBuilt = buildMessageFileChatOrder(room, member, offer,order.getPdfUrl());
            if (resultBuilt) {
                offerService.save(offer
                        .withStatus(OfferStatusEnum.SENT)
                        .withGlobalStatus(OfferGLobalStatusEnum.WORKING));
            }
        }

        return resultBuilt;
    }

    private boolean buildMessageFileChatOrder(ChatRoom chatRoom, User member, Offer offer, final String url) {
        if (Objects.nonNull(chatRoom)) {
            File file = fileService
                    .save(File.builder()
                            .text("")
                            .name("O - "+offer.getName())
                            .url(url)
                            .extension("pdf")
                            .persons(Objects.nonNull(offer.getCustomer().getPerson()) ? List.of(offer.getCustomer().getPerson()) : null)
                            .creationTime(LocalDateTime.now())
                            .creationUser(securityManagementService.findCurrentUser())
                            .build());

            messageService.save(ChatMessage.builder()
                    .chatRoom(chatRoom)
                    .value(" Here is your Order " + member.getName())
                    .sender(securityManagementService.findUserById(ID_USER_SYSTEM))
                    .type(ChatMessageTypeEnum.FILE)
                    .files(List.of(fileApiMapper.modelToResponse(file)))
                    .creationTime(LocalDateTime.now())
                    .build());
            return true;
        }
        return false;
    }

    private ChatRoom validateChatRoom(User user) {
        ChatRoom chatRoom = roomService.findByIdCustomer(user.getCustomer().getId());
        if (Objects.isNull(chatRoom)) {
            throw new BusinessRuleException("ChatRoom not exists");
        }
        return chatRoom;
    }

    private void acceptAllOffers(final Order order) {
        final User currentUser = securityManagementService.findCurrentUser();
        final Offer offer = offerService.findById(order.getOffer().getId());

        if(Objects.nonNull(offer)){
            offerService
                    .save(
                            offer.withStatus(OfferStatusEnum.ACCEPTED)
                                    .withModificationUser(currentUser)
                                    .withModificationTime(LocalDateTime.now()));
        }

        final List<Offer> offers = offerService.findByNumber(offer.getNumber());
        offers.forEach(o ->
                offerService
                        .save(
                                o.withGlobalStatus(OfferGLobalStatusEnum.ACCEPTED)
                                        .withModificationUser(currentUser)
                                        .withModificationTime(LocalDateTime.now()))
        );
    }

    private Offer saveOfferConvertOrderStatusAccepted(User currentUser, final Offer offer) {
        return offerService.save(offer
                .withStatus(OfferStatusEnum.ACCEPTED)
                .withGlobalStatus(OfferGLobalStatusEnum.ACCEPTED)
                .withModificationUser(currentUser)
                .withModificationTime(LocalDateTime.now()));
    }

    public void changeStatus(final Integer id, final Integer statusId) {
        final OrderStatusEnum status = OrderStatusEnum.getById(statusId);
        if (Objects.isNull(status)) {
            throw new StatusNotFoundException(statusId);
        }
        final Order order = orderService.findById(id);
        orderService.save(order
                .withStatus(status)
                .withModificationUser(securityManagementService.findCurrentUser())
                .withModificationTime(LocalDateTime.now()));
    }

    private Order build(final Order order) {
        final Order fromDDBB = Objects.nonNull(order.getId())
                ? orderService
                .findById(order.getId())
                .withNotes(order.getNotes())
                .withPaymentDetails(order.getPaymentDetails())
                //.withRestricted(order.getRestricted())
                : orderService.save(order);
        return fromDDBB
                .withDescription(StringUtils.isNotBlank(order.getDescription()) ? order.getDescription() : null)
                .withStatus(
                        Objects.isNull(order.getId())
                                ? OrderStatusEnum.NEW
                                : fromDDBB.getStatus()
                )
                .withCreationTime(
                        Objects.isNull(order.getId())
                                ? LocalDateTime.now()
                                : fromDDBB.getCreationTime()
                )
                .withDeletedStatus(
                        Objects.isNull(order.getDeletedStatus())
                                ? DeletedStatus.builder().id(DeletedStatusEnum.ACTIVE.getId())
                                .name("Active").build()
                                : order.getDeletedStatus()
                )
                .withModificationTime(
                        Objects.nonNull(order.getId())
                                ? LocalDateTime.now()
                                : null
                )
                .withCreationUser(
                        Objects.isNull(order.getId())
                                ? securityManagementService.findCurrentUser()
                                : fromDDBB.getCreationUser()
                )
                .withModificationUser(
                        Objects.nonNull(order.getId())
                                ? securityManagementService.findCurrentUser()
                                : null
                );
    }

    private void validations(final Order order) {
        validateOffer(order);
    }

    private void validateOffer(final Order order) {
        final Offer offer = offerService.findById(order.getOffer().getId());
        List<Order> orders = orderService.findByOffer(offer);

        if(!CollectionUtils.isEmpty(orders)){
            throw new BusinessRuleException("The selected offer already has an Order generated. Please check.");
        }

        if (Objects.isNull(order.getOffer()) || Objects.isNull(order.getOffer().getId())) {
            throw new FieldIsMissingException(CommonConstants.OFFER_ID);
        }


        if (OfferGLobalStatusEnum.ACCEPTED == offer.getGlobalStatus()) {
            throw new BusinessRuleException("The selected offer already has an Order generated. Please check.");
        }

        if (OfferGLobalStatusEnum.DECLINED == offer.getGlobalStatus()) {
            throw new BusinessRuleException("The selected offer is declined and cannot generate an order.");
        }

        if (OfferGLobalStatusEnum.WORKING == offer.getGlobalStatus() && OfferStatusEnum.CONFIRMED == offer.getStatus()) {
            throw new BusinessRuleException("Please send the offer to the client before generating the Order.");
        }

        if (OfferGLobalStatusEnum.WORKING == offer.getGlobalStatus() && OfferStatusEnum.OPEN == offer.getStatus()) {
            throw new BusinessRuleException("The selected offer is still a draft. Please create the pdf before generating the order.");
        }
    }

    public List<Order> findAll(final OrderSpecification specification,
                               final Integer page, final Integer size, final String direction, final String attribute) {
        final Direction directionEnum =
                Arrays.stream(Direction.values()).anyMatch(v -> v.name().equalsIgnoreCase(direction))
                        ? Direction.fromString(direction)
                        : Direction.DESC;
        final Sort sort = Sort.by(directionEnum, attribute);
        final Pageable pageable = PageRequest.of(page, size, sort);
        return orderService.findAll(specification, pageable)
                .stream()
                .collect(Collectors.toList());
    }

    public Map<String, String> getPdfById(final Integer id) throws IOException {
        final Order order = orderService.findById(id);
        final Offer offer = order.getOffer();

        if (!(offer.getGlobalStatus() == OfferGLobalStatusEnum.WORKING && offer.getStatus() == OfferStatusEnum.ACCEPTED
                && (Objects.isNull(offer.getExpirationTime()) || offer.getExpirationTime().isAfter(LocalDateTime.now())))) {
            throw new BusinessRuleException("Cannot generate pdf. Offer isn't open or is expired");
        }
        final String reportUrl = reportService.exportOrderPdf(order, offer);
        orderService.save(
                order
                        .withPdfUrl(reportUrl)
                        .withModificationTime(LocalDateTime.now())
                        .withModificationUser(securityManagementService.findCurrentUser())
        );
        return Map.of("url", reportUrl);

    }

    public Map<String, String> getPdfInternal(final Integer id) throws IOException {
        final Order order = orderService.findById(id);
        final Offer offer = order.getOffer();

        if (!(offer.getGlobalStatus() == OfferGLobalStatusEnum.WORKING)) {
            throw new BusinessRuleException("Cannot generate pdf. Offer isn't open or is expired");
        }
        final String reportUrl = reportService.exportOrderPdfInternal(order, offer);

        return Map.of("url", reportUrl);

    }

    private void getFileMessage(final Order order, User currentUser) {
        if (!CollectionUtils.isEmpty(order.getOffer().getFiles())) {
            order.getOffer().getFiles()
                    .forEach(file -> fileService.save(File.builder()
                            .url(file.getUrl())
                            .extension(file.getExtension())
                            .persons(Objects.nonNull(order.getOffer().getCustomer()) ?
                                    List.of(order.getOffer().getCustomer().getPerson()) : null)
                            .name(file.getName())
                            .status(DeletedStatus.builder().id(1).build())
                            .creationUser(currentUser)
                            .creationTime(LocalDateTime.now())
                            .build()));
        }
    }

    private static String getNameUrlPdf(String url) {
        String uuid = "";
        // Encuentra la posición del último "/" y ".pdf" en la URL
        int lastSlashIndex = url.lastIndexOf("/");
        int pdfIndex = url.indexOf(".pdf");

        // Extrae el UUID de la URL
        if (lastSlashIndex != -1 && pdfIndex != -1) {
            uuid = url.substring(lastSlashIndex + 1, pdfIndex);

        }
        return uuid;
    }

    public Note saveNote(final Integer id, final Note note) {
        orderService.existsById(id);

        final User currentUser = securityManagementService.findCurrentUser();
        final Note toSave = Objects.nonNull(note.getId())
                ? getPersistedNote(id, note, currentUser)
                : note.withType(NoteTypeEnum.ORDER).withElementId(id);

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

    public void deleteById(final Integer id) {
        final Order model = orderService.findById(id);
        if (Objects.nonNull(model)) {
            throw new BusinessRuleException(CommonConstants.IS_A_CUSTOMER_MESSAGE);
        }
        orderService.deleteById(id);
    }

    public void deleteByStatus(final Integer id) {
        final Order model = orderService.findById(id);
        if (Objects.nonNull(model)) {
            orderService.deleteStatus(id);
            deleteNoteAssociateProduct(model.getId());
        }

    }

    private void deleteNoteAssociateProduct(Integer id) {
        List<Note> notes = noteService.finByElementId(id);
        if (Objects.nonNull(notes)) {
            notes.forEach(note -> {
                noteService.deleteStatus(note.getId());
            });
        }
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

        if (byId.getType() != NoteTypeEnum.ORDER) {
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
