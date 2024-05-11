package com.martzatech.vdhg.crmprojectback.application.services.mobil;

import com.martzatech.vdhg.crmprojectback.application.enums.UserTypeEnum;
import com.martzatech.vdhg.crmprojectback.application.exceptions.BusinessRuleException;
import com.martzatech.vdhg.crmprojectback.domains.chat.enums.ChatMessageTypeEnum;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.ChatMessage;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.ChatRoom;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.File;
import com.martzatech.vdhg.crmprojectback.domains.chat.services.ChatMessageService;
import com.martzatech.vdhg.crmprojectback.domains.chat.services.ChatRoomService;
import com.martzatech.vdhg.crmprojectback.domains.chat.services.FileService;
import com.martzatech.vdhg.crmprojectback.domains.customers.enums.OfferGLobalStatusEnum;
import com.martzatech.vdhg.crmprojectback.domains.customers.enums.OfferStatusEnum;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Associated;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Customer;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.GroupAccount;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Offer;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.AssociatedService;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.CustomerAppService;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.GroupAccountService;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.mobil.OfferMobileService;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.OfferV2MobilSpecification;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
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
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
@Service
public class OfferManagementAppService {
    private OfferMobileService offerService;
    private SecurityManagementAppService securityManagementService;
    private CustomerAppService customerAppService;
    private AssociatedService associatedService;
    private ChatRoomService chatRoomService;
    private GroupAccountService groupAccountService;
    private ChatMessageService messageService;
    private FileService fileService;
    private FileApiMapper fileApiMapper;



    @Transactional
    public List<Offer> findAll(final OfferV2MobilSpecification specification,
                               final Integer page, final Integer size, final String direction,
                               final String attribute) {
        final Direction directionEnum =
                Arrays.stream(Direction.values()).anyMatch(v -> v.name().equalsIgnoreCase(direction))
                        ? Direction.fromString(direction)
                        : Direction.DESC;
        final Sort sort = Sort.by(directionEnum, attribute);
        final Pageable pageable = PageRequest.of(page, size, sort);

        return offerService.findAll(specification, pageable)
                .stream()
                .collect(Collectors.toList());
    }
    public boolean sendMobileChatService(final Integer id,final String token) {
        Boolean messageSent = Boolean.FALSE;
        User user = null;
        Customer customer=null;
        Associated associated;
        user = getUserValidate(token, user);
        offerService.existsById(id);
        Offer offer = offerService.findById(id);

        validateStatusSent(offer);

        var pdfUrl = offer.getPdfUrl();
        boolean restricted = offer.getRestricted();
        File file = fileService.findByUrl(pdfUrl);
        ChatRoom room = null;
        return getTypeUser(token, user, restricted, offer, file, messageSent);
    }

    private Boolean getTypeUser(String token, User user, boolean restricted, Offer offer, File file, Boolean messageSent) {
        Associated associated;
        ChatRoom room;
        Customer customer;
        if (user.getTypeUser().equals(UserTypeEnum.CUSTOMER.name())) {
            customer = customerAppService.findById(user.getCustomer().getId(), token);

            if(Objects.nonNull(customer.getGroupAccount())){
                var groupId = customer.getGroupAccount().getId();
                GroupAccount account = groupAccountService.findById(groupId);

                if(restricted){
                    messageSent = sentChatClient(user, offer, file, customer);
                }else{
                    messageSent = sentChatGroupAccount(user, offer, file, account);
                }

            }else{
                messageSent = sentChatClient(user, offer, file, customer);
            }
        }else{
            associated = associatedService.findById(user.getAssociated().getId());
            if(Objects.nonNull(associated) && Objects.nonNull(associated.getGroupAccount())){
                var groupId = user.getAssociated().getGroupAccount().getId();
                GroupAccount account = groupAccountService.findById(groupId);
                messageSent = sentChatGroupAccount(user, offer, file, account);
            }
        }
        return messageSent;
    }

    private Boolean sentChatGroupAccount(User user, Offer offer, File file, GroupAccount account) {
        ChatRoom room;
        Boolean messageSent;
        room = chatRoomService.findByIdGroupAccount(account);
        buildMessageCustomerChat(room, offer, user, file);
        messageSent = Boolean.TRUE;
        return messageSent;
    }

    private Boolean sentChatClient(User user, Offer offer, File file, Customer customer) {
        ChatRoom room;
        Boolean messageSent;
        room = chatRoomService.findByIdCustomer(customer.getId());
        buildMessageCustomerChat(room, offer, user, file);
        messageSent = Boolean.TRUE;
        return messageSent;
    }

    private static void validateStatusSent(Offer offer) {
        if(!(offer.getStatus().equals(OfferStatusEnum.SENT) && offer.getGlobalStatus().equals(OfferGLobalStatusEnum.WORKING))){
            throw new BusinessRuleException("Offer isn't status Send or Working");
        }
    }

    private User getUserValidate(String token, User user) {
        if (securityManagementService.validateToken(token)) {
            user = securityManagementService.findCurrentUser(token);
        }
        return user;
    }

    private boolean buildMessageCustomerChat(ChatRoom chatRoom, Offer offer,User sender,File file) {
        if (Objects.nonNull(chatRoom)) {
            messageService.save(ChatMessage.builder().chatRoom(chatRoom)
                    .value(" I would like to receive more information about this proposal: " + offer.getName())
                    .sender(sender)
                    .type(ChatMessageTypeEnum.FILE)
                    .files(List.of(fileApiMapper.modelToResponse(file)))
                    .build());
            return true;
        }
        return false;
    }

}
