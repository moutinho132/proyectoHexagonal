package com.martzatech.vdhg.crmprojectback.application.services;

import com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants;
import com.martzatech.vdhg.crmprojectback.application.enums.DeletedStatusEnum;
import com.martzatech.vdhg.crmprojectback.application.enums.UserTypeEnum;
import com.martzatech.vdhg.crmprojectback.application.exceptions.BusinessRuleException;
import com.martzatech.vdhg.crmprojectback.application.exceptions.FieldIsMissingException;
import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.UserNotFoundException;
import com.martzatech.vdhg.crmprojectback.domains.chat.enums.ChatMessageTypeEnum;
import com.martzatech.vdhg.crmprojectback.domains.chat.enums.ChatRoomTypeEnum;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.ChatMessage;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.ChatRoom;
import com.martzatech.vdhg.crmprojectback.domains.chat.services.ChatMessageService;
import com.martzatech.vdhg.crmprojectback.domains.chat.services.ChatRoomService;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Customer;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.GroupAccount;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.CustomerService;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.GroupAccountService;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.GroupAccountSpecification;
import com.martzatech.vdhg.crmprojectback.domains.security.models.DeletedStatus;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import com.martzatech.vdhg.crmprojectback.domains.security.models.UserStatus;
import com.martzatech.vdhg.crmprojectback.domains.security.services.RoleService;
import com.martzatech.vdhg.crmprojectback.domains.security.services.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
@Service
public class GroupAccountManagementService {

    public static final int ID = 25;
    private final CustomerService customerService;
    private final GroupAccountService groupAccountService;
    private final SecurityManagementService securityManagementService;
    private final ChatManagementService chatManagementService;
    private ChatMessageService messageService;
    private final RoleService roleService;
    private final UserService userService;
    private final ChatRoomService roomService;
    public static final int ID_USER_SYSTEM = 2;

    /*
     * The core services
     */
    @Transactional
    public GroupAccount save(final GroupAccount model) {
        validations(model);
        GroupAccount accountSave = groupAccountService.save(build(model));
        if (Objects.isNull(model.getId())) {
            ChatRoom chatRoom = saveGroupAccountChatRoom(accountSave);
            if(Objects.nonNull(chatRoom)){
                Customer customer = customerService.findById(model.getOwner().getId());
                buildMessageCustomerChat(chatRoom,accountSave,customer);
            }
        }
        return accountSave;
    }

    private ChatRoom saveGroupAccountChatRoom(GroupAccount account) {
        validateGroupAccountChatRoomExist(account.getOwner().getId());
        User user =  userService.findByIdCustomer(account.getOwner().getId());
        if(Objects.isNull(user)){
            throw new UserNotFoundException(account.getOwner().getId());
        }
        return chatManagementService.saveRoom(ChatRoom
                .builder()
                .name(account.getName())
                .members(List.of(buildUser(account,user)))
                .type(ChatRoomTypeEnum.GROUPACCOUNT)
                .groupAccount(Objects.nonNull(account) ? account : null)
                .customer(Objects.nonNull(account.getOwner()) ? account.getOwner():null)
                .build());
    }

    private User buildUser(GroupAccount account,User user) {

        return Objects.nonNull(user) ? User
                .builder()
                .id(user.getId())
                .email(account.getEmail())
                .surname("")
                .name(account.getName())
                .roles(List.of(roleService.findById(3)))
                .status(UserStatus.builder().id(DeletedStatusEnum.ACTIVE.getId()).build())
                .customer(account.getOwner())
                .typeUser(UserTypeEnum.CUSTOMER.name())
                .modificationTime(account.getModificationTime())
                .modificationUser(account.getModificationUser())
                .creationTime(account.getCreationTime())
                .creationUser(account.getCreationUser())
                .build() : null;
    }


    private void buildMessageCustomerChat(ChatRoom chatRoom, GroupAccount member,Customer customer) {
        if(Objects.nonNull(chatRoom)){
            messageService.save(ChatMessage.builder().chatRoom(chatRoom)
                    .value("Welcome "+ customer.getPerson().getName()+
                            " to your group Chat. Please feel free" +
                            " to ask anything you need").sender(securityManagementService
                            .findUserById(ID_USER_SYSTEM)).type(ChatMessageTypeEnum.SYSTEM).build());
        }
    }
    private void validateGroupAccountChatRoomExist(final Integer id) {
        Long countAssociateChatRoom = chatManagementService.findAllChatRoomAssociate(id);
        if (countAssociateChatRoom.intValue() > 0) {
            throw new BusinessRuleException("It can only contain one Customer per chat.");
        }
    }

    private GroupAccount build(final GroupAccount model) {
        final GroupAccount byId = findById(model);
        return byId
                .withCreationTime(
                        Objects.isNull(model.getId())
                                ? LocalDateTime.now()
                                : byId.getCreationTime()
                ).withCreationUser(
                        Objects.isNull(model.getId())
                                ? securityManagementService.findCurrentUser()
                                : byId.getCreationUser()

                ).withModificationTime(
                        Objects.isNull(model.getId())
                                ? null
                                : LocalDateTime.now()
                ).withModificationUser(
                        Objects.isNull(model.getId())
                                ? null
                                : securityManagementService.findCurrentUser()
                )
                .withChatRoom(Objects.nonNull(byId.getChatRoom())?byId.getChatRoom():null)
                .withStatus(
                        Objects.isNull(byId.getStatus())
                                ? DeletedStatus.builder().id(DeletedStatusEnum.ACTIVE.getId()).build()
                                : byId.getStatus()
                );
    }

    private GroupAccount findById(final GroupAccount model) {
        if (Objects.nonNull(model.getId())) {
            ChatRoom room =  roomService.findByIdGroupAccount(model);
            return groupAccountService.findById(model.getId())
                    .withName(model.getName())
                    .withAlias(model.getAlias())
                    .withIndustry(model.getIndustry())
                    .withVat(model.getVat())
                    .withEmail(model.getEmail())
                    .withChatRoom(Objects.nonNull(room) ? room : null);
        }
        return model;
    }
    @Transactional
    public List<GroupAccount> findAll(final GroupAccountSpecification specification,
                                      final Integer page, final Integer size, final String direction, final String attribute) {
        final Direction directionEnum =
                Arrays.stream(Direction.values()).anyMatch(v -> v.name().equalsIgnoreCase(direction))
                        ? Direction.fromString(direction)
                        : Direction.DESC;
        final Sort sort = Sort.by(directionEnum, attribute);
        final Pageable pageable = PageRequest.of(page, size, sort);
        return groupAccountService.findAll(specification, pageable)
                .stream()
                .map(account ->account.withChatRoom(Objects.nonNull(roomService.findByIdGroupAccount(account))?roomService.findByIdGroupAccount(account):null) )
                .collect(Collectors.toList());
    }

    /*
     * Validators
     */
    private void validations(final GroupAccount model) {
        validOwner(model.getOwner());
        validDuplicated(model);
        validEmail(model);
        validName(model);
    }

    private void validName(final GroupAccount model) {
        if (StringUtils.isNoneEmpty(model.getName())) {
            final GroupAccount persisted = groupAccountService.findByName(model);
            if (Objects.nonNull(persisted)
                    && (Objects.isNull(model.getId()) || persisted.getId().intValue() != model.getId())) {
                throw new BusinessRuleException("Already exists a group account with this name.");
            }
        }
    }

    private void validEmail(final GroupAccount model) {
        if (StringUtils.isNoneEmpty(model.getEmail())) {
            final GroupAccount persisted = groupAccountService.findByEmail(model);
            if (Objects.nonNull(persisted)
                    && (Objects.isNull(model.getId()) || persisted.getId().intValue() != model.getId())) {
                throw new BusinessRuleException("Already exists a group account with this email.");
            }
        }
    }

    private void validDuplicated(final GroupAccount model) {
        final GroupAccount groupAccount = groupAccountService.findByCustomer(model.getOwner());
        if (Objects.nonNull(groupAccount)
                && (Objects.isNull(model.getId()) || model.getId().intValue() != groupAccount.getId())) {
            final String message = String
                    .format(
                            "Currently the customer %s is associated with the group account %s",
                            model.getOwner().getId(), groupAccount.getId()
                    );
            throw new BusinessRuleException(message);
        }
    }

    private void validOwner(final Customer customer) {
        if (Objects.isNull(customer) || Objects.isNull(customer.getId())) {
            throw new FieldIsMissingException(CommonConstants.OWNER_FIELD);
        }
        customerService.existsById(customer.getId());
    }
}
