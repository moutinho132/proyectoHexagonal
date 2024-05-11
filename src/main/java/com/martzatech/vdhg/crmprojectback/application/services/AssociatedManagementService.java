package com.martzatech.vdhg.crmprojectback.application.services;

import com.martzatech.vdhg.crmprojectback.application.enums.UserStatusEnum;
import com.martzatech.vdhg.crmprojectback.application.enums.UserTypeEnum;
import com.martzatech.vdhg.crmprojectback.application.exceptions.BusinessRuleException;
import com.martzatech.vdhg.crmprojectback.application.services.mobil.SecurityManagementAppService;
import com.martzatech.vdhg.crmprojectback.domains.chat.enums.ChatMessageTypeEnum;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.ChatMessage;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.ChatRoom;
import com.martzatech.vdhg.crmprojectback.domains.chat.services.ChatMessageService;
import com.martzatech.vdhg.crmprojectback.domains.chat.services.ChatRoomService;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Associated;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Customer;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.GroupAccount;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.AssociatedService;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.CustomerService;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.GroupAccountService;
import com.martzatech.vdhg.crmprojectback.domains.security.mappers.UserMapper;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import com.martzatech.vdhg.crmprojectback.domains.security.models.UserMobil;
import com.martzatech.vdhg.crmprojectback.domains.security.models.UserStatus;
import com.martzatech.vdhg.crmprojectback.domains.security.services.RoleService;
import com.martzatech.vdhg.crmprojectback.domains.security.services.UserMobilService;
import com.martzatech.vdhg.crmprojectback.domains.security.services.UserService;
import com.martzatech.vdhg.crmprojectback.infrastructure.configs.Jwt;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.martzatech.vdhg.crmprojectback.application.services.mobil.SecurityManagementAppService.generateSHA256Hash;

@AllArgsConstructor
@Slf4j
@Service
public class AssociatedManagementService {
    public static final int ID_USER_SYSTEM = 2;
    public static final int ID = 4;
    public static final String EMAIL = "associated@infomartzatech.onmicrosoft.com";
    private final GroupAccountService groupAccountService;
    private final CustomerService customerService;
    private final AssociatedService associatedService;
    private final SecurityManagementService securityManagementService;
    private final ChatManagementService chatManagementService;
    private final RoleService roleService;
    private ChatMessageService messageService;
    private GroupAccountManagementService accountManagementService;
    private ChatRoomService chatRoomService;
    private final UserService userService;
    private final Jwt jwt;
    private final UserMobilService mobilService;
    private final UserMapper userMapper;
    private final SecurityManagementAppService securityManagementAppService;

    /*
     * The core services
     */
    @Transactional
    public Associated save(final Integer groupAccountId, final Associated model) {
        validations(groupAccountId, model);
        GroupAccount account = groupAccountService.findById(groupAccountId);
        List<User> newMemberAssociate = new ArrayList<>();
        User memberAssociate = null;
        Associated associatedSaveIntoDb = associatedService.save(
                build(model).withGroupAccount(account));

        if(Objects.nonNull(associatedSaveIntoDb)  ){
             memberAssociate = securityManagementService
                     .saveUser(buildUserAssociated(associatedSaveIntoDb,  userService
                             .findByIdAssociated(associatedSaveIntoDb.getId())));

             if(Objects.isNull(model.getId())){
                 securityManagementAppService.saveUserMobil(buildUserMobil(associatedSaveIntoDb));
             }
        }

        if (Objects.isNull(model.getId())) {
            ChatRoom chatRoomGroupAccount = chatRoomService.findByIdGroupAccount(groupAccountService.findById(groupAccountId));
            if (Objects.nonNull(chatRoomGroupAccount)) {
                validateGroupAccountChatRoomExist(memberAssociate.getId());
                newMemberAssociate = !CollectionUtils.isEmpty(chatRoomGroupAccount.getMembers()) ?chatRoomGroupAccount.getMembers().stream()
                        .filter(user -> user.getTypeUser().equals(UserTypeEnum.CUSTOMER.name())
                        || user.getTypeUser().equals(UserTypeEnum.ASSOCIATE.name()))
                        .collect(Collectors.toList()):null;

                if(Objects.nonNull(newMemberAssociate)){
                    newMemberAssociate.add(memberAssociate);
                }
                if(!CollectionUtils.isEmpty(chatRoomGroupAccount.getMembers())){
                    chatRoomGroupAccount.getMembers().clear();
                    chatRoomGroupAccount.getMembers().addAll(newMemberAssociate);
                }
                ChatRoom chatRoomSave = chatManagementService.saveRoom(chatRoomGroupAccount);
                if(Objects.nonNull(chatRoomSave)){
                    buildMessageAssociateChatRoom(chatRoomSave, memberAssociate);
                }
            }
        }
        return associatedSaveIntoDb;
    }

    private UserMobil buildUserMobil(Associated associated) {
        User userCustomer = userService.findByIdAssociated(associated.getId());
        return UserMobil.builder()
                .userName(userCustomer.getEmail())
                .password(generateSHA256Hash(associated.getGroupAccount().getOwner()
                        .getPerson().getIdentityDocuments()
                        .stream()
                        .map(identityDocument -> identityDocument.getValue()).findAny().get()))
                .user(userMapper.modelToModelNew(userCustomer))
                .passwordUpdateRequired(false)
                .build();
    }

    private void buildMessageAssociateChatRoom(final ChatRoom chatRoomSave,final User memberAssociate) {
        if(Objects.nonNull(chatRoomSave)){
            messageService.save(ChatMessage.builder().chatRoom(chatRoomSave)
                    .value("Welcome "+ memberAssociate.getName()+
                            " to the Concierge Chat. Please feel free" +
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


    public User buildUserAssociated(final Associated model,User user) {
        return User.builder()
                .id(Objects.nonNull(user) ? user.getId() : null)
                .title("-")
                .name(model.getName())
                .surname(model.getSurname())
                .email(model.getEmail())
                .mobile(model.getPhonePrefix() + "" + model.getPhoneNumber())
                .roles(List.of(roleService.findById(ID)))
                .associated(model.withEmail(EMAIL))
                .typeUser(UserTypeEnum.ASSOCIATE.name())
                .status(UserStatus.builder().id(UserStatusEnum.ACTIVE.getId()).build())
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

    private Associated build(final Associated model) {
        final Associated byId = findById(model);
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
                );
    }

    private Associated findById(final Associated model) {
        if (Objects.nonNull(model.getId())) {
            return associatedService.findById(model.getId())
                    .withName(model.getName())
                    .withSurname(model.getSurname())
                    .withEmail(model.getEmail())
                    .withPosition(model.getPosition())
                    .withMainContact(model.getMainContact())
                    .withPhonePrefix(model.getPhonePrefix())
                    .withPhoneNumber(model.getPhoneNumber())
                    .withPreferredLanguage(model.getPreferredLanguage());
        }
        return model;
    }

    /*
     * Validators
     */
    private void validations(final Integer groupAccountId, final Associated model) {
        validationGroupAccount(groupAccountId);
        validationEmails(model);
        validationMainContact(groupAccountId, model);
    }

    private void validationGroupAccount(final Integer groupAccountId) {
        groupAccountService.existsById(groupAccountId);
    }

    private void validationEmails(final Associated model) {
        if (StringUtils.isNotBlank(model.getEmail())) {
            final Associated associated = associatedService.findByEmail(model.getEmail());
            if (Objects.nonNull(associated)
                    && (Objects.isNull(model.getId()) || associated.getId().intValue() != model.getId())) {
                throw new BusinessRuleException("Already exists an *associated* with this email.");
            }
            final Customer customer = customerService.findByEmail(model.getEmail());
            if (Objects.nonNull(customer)) {
                throw new BusinessRuleException("Already exists a *customer* with this email.");
            }
        }
    }

    private void validationMainContact(final Integer groupAccountId, final Associated model) {
        if (Boolean.TRUE.equals(model.getMainContact())) {
            associatedService.updateMainContactToFalseByGroupAccountId(groupAccountId);
        }
    }

    public void deleteById(final Integer groupAccountId, final Integer associatedId) {
        final GroupAccount groupAccount = groupAccountService.findById(groupAccountId);

        if (CollectionUtils.isEmpty(groupAccount.getAssociates())
                || groupAccount.getAssociates().stream().noneMatch(a -> a.getId().intValue() == associatedId)) {
            throw new BusinessRuleException("This associated does not belong to this groupAccount");
        }

        associatedService.deleteById(associatedId);
    }
}
