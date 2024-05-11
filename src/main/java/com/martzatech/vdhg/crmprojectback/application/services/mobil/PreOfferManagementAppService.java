package com.martzatech.vdhg.crmprojectback.application.services.mobil;

import com.martzatech.vdhg.crmprojectback.application.enums.UserTypeEnum;
import com.martzatech.vdhg.crmprojectback.application.exceptions.BusinessRuleException;
import com.martzatech.vdhg.crmprojectback.application.services.AzureFileService;
import com.martzatech.vdhg.crmprojectback.application.services.ReportPdfService;
import com.martzatech.vdhg.crmprojectback.domains.chat.enums.ChatMessageTypeEnum;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.ChatMessage;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.ChatRoom;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.File;
import com.martzatech.vdhg.crmprojectback.domains.chat.services.ChatMessageService;
import com.martzatech.vdhg.crmprojectback.domains.chat.services.ChatRoomService;
import com.martzatech.vdhg.crmprojectback.domains.chat.services.FileService;
import com.martzatech.vdhg.crmprojectback.domains.customers.enums.OfferStatusEnum;
import com.martzatech.vdhg.crmprojectback.domains.customers.enums.PreOfferStatusEnum;
import com.martzatech.vdhg.crmprojectback.domains.customers.mappers.BockingProductMapper;
import com.martzatech.vdhg.crmprojectback.domains.customers.mappers.PreOfferMapper;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Associated;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Customer;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.GroupAccount;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.PreOffer;
import com.martzatech.vdhg.crmprojectback.domains.customers.repositories.BockingProductRepository;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.*;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.mobil.PreOfferAppService;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.PreOfferV2MobilSpecification;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import com.martzatech.vdhg.crmprojectback.domains.vendors.services.VendorService;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.FileApiMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Slf4j
@Service
public class PreOfferManagementAppService {

    public static final int GLOBAL_STATUS_CLOSE = 2;
    public static final int GLOBAL_STATUS_IN_PROGRESS = 1;
    public static final String CANNOT_GENERATE_PDF_PRE_OFFER_ISN_T_OPEN_OR_IS_EXPIRED = "Cannot generate pdf. PreOffer isn't open or is expired";
    private final FileService fileService;
    private final AzureFileService azureFileService;
    private PreOfferAppService preOfferService;
    private OfferService offerService;
    private ProductService productService;
    private ReportPdfService reportService;
    private BockingProductMapper priceMapper;
    private VendorService vendorService;
    private BockingProductRepository priceRepository;
    private FileApiMapper fileApiMapper;
    private final PreOfferMapper preOfferMapper;
    private SecurityManagementAppService securityManagementService;
    private CustomerAppService customerAppService;
    private GroupAccountService groupAccountService;
    private ChatRoomService chatRoomService;
    private AssociatedService associatedService;
    private ChatMessageService messageService;


    @Transactional
    public List<PreOffer> findAll(final PreOfferV2MobilSpecification specification,
                                  final Integer page, final Integer size, final String direction,
                                  final String attribute) {
        final Direction directionEnum =
                Arrays.stream(Direction.values()).anyMatch(v -> v.name().equalsIgnoreCase(direction))
                        ? Direction.fromString(direction)
                        : Direction.DESC;
        final Sort sort = Sort.by(directionEnum, attribute);
        final Pageable pageable = PageRequest.of(page, size, sort);
        return preOfferService.findAll(specification, pageable);
    }

    public boolean sendMobileChatService(final Integer id, final String token) {
        Boolean messageSent = Boolean.FALSE;
        User user = null;
        user = getUserValidate(token, user);
        preOfferService.existsById(id);
        PreOffer preOffer = preOfferService.findById(id);

        validateStatusSent(preOffer);
        var pdfUrl = preOffer.getPdfUrl();
        File file = fileService.findByUrl(pdfUrl);
        return getTypeUser(token, user, preOffer, file, messageSent);
    }

    private Boolean getTypeUser(String token, User user, PreOffer preOffer, File file, Boolean messageSent) {
        ChatRoom room;
        Customer customer;
        Associated associated;
        if (user.getTypeUser().equals(UserTypeEnum.CUSTOMER.name())) {
            customer = customerAppService.findById(user.getCustomer().getId(), token);

            if (Objects.nonNull(customer.getGroupAccount())) {
                var groupId = customer.getGroupAccount().getId();
                GroupAccount account = groupAccountService.findById(groupId);
                messageSent = sendChatGroupAccount(user, preOffer, file, account);
            } else {
                messageSent = sendChatClient(user,preOffer,file,customer);
            }
        } else {
            associated = associatedService.findById(user.getAssociated().getId());
            messageSent = getGroupAccountAndSend(user, preOffer, file, messageSent, associated);
        }
        return messageSent;
    }

    private Boolean sendChatGroupAccount(User user, PreOffer preOffer, File file, GroupAccount account) {
        Boolean messageSent;
        ChatRoom room;
        room = chatRoomService.findByIdGroupAccount(account);
        buildMessageCustomerChat(room, preOffer, user, file);
        messageSent = Boolean.TRUE;
        return messageSent;
    }

    private Boolean sendChatClient(User user, PreOffer preOffer, File file, Customer customer) {
        ChatRoom room;
        Boolean messageSent;
        room = chatRoomService.findByIdCustomer(customer.getId());
        buildMessageCustomerChat(room, preOffer, user, file);
        messageSent = Boolean.TRUE;
        return messageSent;
    }

    private Boolean getGroupAccountAndSend(User user, PreOffer preOffer, File file, Boolean messageSent, Associated associated) {
        ChatRoom room;
        if (Objects.nonNull(associated) && Objects.nonNull(associated.getGroupAccount())) {
            var groupId = user.getAssociated().getGroupAccount().getId();
            GroupAccount account = groupAccountService.findById(groupId);
            room = chatRoomService.findByIdGroupAccount(account);
            buildMessageCustomerChat(room, preOffer, user, file);
            messageSent = Boolean.TRUE;
        }
        return messageSent;
    }

    private static void validateStatusSent(PreOffer preOffer) {
        if (!(preOffer.getStatus().equals(OfferStatusEnum.SENT) && preOffer.getGlobalStatus().equals(PreOfferStatusEnum.WORKING))) {
            throw new BusinessRuleException("Offer isn't status Send or Working");
        }
    }

    private User getUserValidate(String token, User user) {
        if (securityManagementService.validateToken(token)) {
            user = securityManagementService.findCurrentUser(token);
        }
        return user;
    }

    private boolean buildMessageCustomerChat(ChatRoom chatRoom, PreOffer preOffer, User sender, File file) {
        if (Objects.nonNull(chatRoom)) {
            messageService.save(ChatMessage.builder()
                    .chatRoom(chatRoom)
                    .value(" I would like to receive more information about this this recommendation: " + preOffer.getName())
                    .sender(sender)
                    .type(ChatMessageTypeEnum.FILE)
                    .files(List.of(fileApiMapper.modelToResponse(file)))
                    .build());
            return true;
        }
        return false;
    }
}
