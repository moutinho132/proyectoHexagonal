package com.martzatech.vdhg.crmprojectback.domains.security.mappers;

import com.martzatech.vdhg.crmprojectback.domains.chat.entities.ChatMessageEntity;
import com.martzatech.vdhg.crmprojectback.domains.chat.entities.ChatMessageReaderEntity;
import com.martzatech.vdhg.crmprojectback.domains.chat.entities.ChatRoomEntity;
import com.martzatech.vdhg.crmprojectback.domains.chat.entities.FileEntity;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.ChatMessage;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.ChatMessageReader;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.ChatRoom;
import com.martzatech.vdhg.crmprojectback.domains.commons.entities.PersonEntity;
import com.martzatech.vdhg.crmprojectback.domains.commons.models.*;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.AssociatedEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.CustomerEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.GroupAccountEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.MembershipEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Associated;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Customer;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.GroupAccount;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Membership;
import com.martzatech.vdhg.crmprojectback.domains.security.entities.UserEntity;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import com.martzatech.vdhg.crmprojectback.domains.security.models.UserNew;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.FileResponse;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.springframework.util.CollectionUtils;

import java.util.Objects;
import java.util.stream.Collectors;

@Mapper
public interface CommonNamed {

    @Named("customAssociateMapping")
    default ChatRoomEntity customAssociateMapping(final ChatRoom model) {
        return Objects.nonNull(model)
                ? ChatRoomEntity.builder()
                .id(model.getId())
                .name(model.getName())
                .customer(Objects.nonNull(model.getCustomer()) ? CustomerEntity
                        .builder()
                        .id(model.getCustomer().getId())
                        .membership(Objects.nonNull(model.getCustomer().getMembership()) ?
                                MembershipEntity.builder()
                                        .id(model.getCustomer().getMembership().getId())
                                        .name(model.getCustomer().getMembership().getName())
                                        .priority(model.getCustomer().getMembership().getPriority())
                                        .price(model.getCustomer().getMembership().getPrice()).build() : null).build() : null)
                .groupAccount(Objects.nonNull(model.getGroupAccount()) ?
                        GroupAccountEntity.builder().id(model.getGroupAccount().getId()).build() : null)
                .build() : null;
    }

    @Named("customAssociateModelMapping")
    default ChatRoom customAssociateModelMapping(final ChatRoomEntity entity) {
        return Objects.nonNull(entity)
                ? ChatRoom.builder()
                .id(entity.getId())
                .name(entity.getName())
                .type(entity.getType())
                .customer(Objects.nonNull(entity.getCustomer()) ? Customer
                        .builder()
                        .id(entity.getCustomer().getId())
                        .membership(Objects.nonNull(entity.getCustomer().getMembership()) ?
                                Membership.builder()
                                        .id(entity.getCustomer().getMembership().getId())
                                        .name(entity.getCustomer().getMembership().getName())
                                        .priority(entity.getCustomer().getMembership().getPriority())
                                        .price(entity.getCustomer().getMembership().getPrice()).build() : null).build() : null)
                .groupAccount(Objects.nonNull(entity.getGroupAccount()) ?
                        GroupAccount.builder().id(entity.getGroupAccount().getId()).build() : null)
                .build() : null;
    }

    @Named("customGroupAccountMapping")
    default GroupAccount customGroupAccountMapping(final GroupAccountEntity entity) {
        return Objects.nonNull(entity)
                ? GroupAccount.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build() : null;
    }

    @Named("customGroupAccountReverMapping")
    default GroupAccountEntity customGroupAccountReverMapping(final GroupAccount model) {
        return Objects.nonNull(model)
                ? GroupAccountEntity.builder()
                .id(model.getId())
                .name(model.getName())
                .build() : null;
    }

    /**
     * @param entity
     * @return
     */
    @Named("customUserMapping")
    static User customUserMapping(final UserEntity entity) {
        return Objects.nonNull(entity)
                ? User
                .builder()
                .id(Objects.nonNull(entity.getId()) ? entity.getId() : null)
                .title(Objects.nonNull(entity.getTitle()) ? entity.getTitle() : null)
                .name(Objects.nonNull(entity.getName()) ? entity.getName() : null)
                .surname(Objects.nonNull(entity.getSurname()) ? entity.getSurname() : null)
                .email(Objects.nonNull(entity.getEmail()) ? entity.getEmail() : null)
                .address(Objects.nonNull(entity.getAddress()) ? entity.getAddress() : null)
                .mobile(Objects.nonNull(entity.getMobile()) ? entity.getMobile() : null)
                .customer(Objects.nonNull(entity.getCustomer()) ? Customer.builder()
                        .id(entity.getCustomer().getId())
                        .membership(Objects.nonNull(entity.getCustomer().getMembership()) ? Membership.builder()
                                .id(entity.getCustomer().getMembership().getId())
                                .priority(entity.getCustomer().getMembership().getPriority()).build() : null)
                        .build() : null)
                .associated(Objects.nonNull(entity.getAssociated()) ? Associated.builder().id(entity.getAssociated().getId()).build() : null)
                .typeUser(Objects.nonNull(entity.getTypeUser()) ? entity.getTypeUser() : null)
                .build()
                : null;
    }


    @Named("customUserModelMapping")
    static UserEntity customUserMapping(final User model) {
        return Objects.nonNull(model)
                ? UserEntity
                .builder()
                .id(model.getId())
                .title(StringUtils.isNotBlank(model.getTitle()) ? model.getTitle() : null)
                .name(model.getName())
                .surname(model.getSurname())
                .email(model.getEmail())
                .address(model.getAddress())
                .mobile(model.getMobile())
                .customer(Objects.nonNull(model.getCustomer()) ? CustomerEntity.builder()
                        .id(model.getCustomer().getId())
                        .membership(MembershipEntity.builder()
                                .id(model.getCustomer().getMembership().getId())
                                .priority(model.getCustomer().getMembership().getPriority()).build())
                        .build() : null)
                .associated(Objects.isNull(model.getAssociated()) ? null :
                        AssociatedEntity.builder().id(model.getAssociated().getId())
                                .build())
                .typeUser(Objects.nonNull(model.getTypeUser()) ? model.getTypeUser() : null)
                .build()
                : null;
    }

    @Named("customMessageModelMapping")
    static ChatMessageEntity customMessageModelMapping(final ChatMessage model) {
        return Objects.nonNull(model)
                ? ChatMessageEntity
                .builder()
                .chatRoom(Objects.isNull(model.getChatRoom()) ? ChatRoomEntity.builder()
                        .id(model.getChatRoom().getId()).build() : null)
                .creationTime(Objects.nonNull(model.getCreationTime())
                        ? model.getCreationTime() : null)
                .files(!CollectionUtils.isEmpty(model.getFiles())
                        ? model.getFiles().stream().map(fileResponse ->
                        FileEntity.builder().id(fileResponse.getId()).build()).collect(Collectors.toList()) : null)
                .id(model.getId())
                .readers(Objects.nonNull(model.getReaders())
                        ? model.getReaders().stream()
                        .map(chatMessageReader -> ChatMessageReaderEntity.builder()
                                .id(chatMessageReader.getId())
                                .message(Objects.nonNull(chatMessageReader.getMessage()) ? ChatMessageEntity
                                        .builder()
                                        .id(chatMessageReader.getMessage().getId())
                                        .type(chatMessageReader.getMessage().getType())
                                        .chatRoom(Objects.nonNull(chatMessageReader.getMessage().getChatRoom())
                                                ? ChatRoomEntity.builder()
                                                .id(chatMessageReader.getMessage().getChatRoom().getId())
                                                .build() : null)
                                        .build() : null)
                                .readingTime(Objects.isNull(chatMessageReader.getMessage()) ? null : chatMessageReader.getMessage().getReadingTime())
                                .build()).collect(Collectors.toList()) : null)
                .build()
                : null;
    }

    @Named("customMessageEntityMapping")
    static ChatMessage customMessageEntityMapping(final ChatMessageEntity entity) {
        return Objects.nonNull(entity)
                ? ChatMessage
                .builder()
                .chatRoom(Objects.nonNull(entity.getChatRoom()) ? ChatRoom.builder()
                        .id(entity.getChatRoom().getId()).build() : null)
                .creationTime(Objects.nonNull(entity.getCreationTime())
                        ? entity.getCreationTime() : null)
                .files(!CollectionUtils.isEmpty(entity.getFiles())
                        ? entity.getFiles().stream().map(fileResponse ->
                        FileResponse.builder().id(fileResponse.getId()).build()).collect(Collectors.toList()) : null)
                .id(entity.getId())
                .readers(Objects.nonNull(entity.getReaders())
                        ? entity.getReaders().stream()
                        .map(chatMessageReader -> ChatMessageReader.builder()
                                .id(chatMessageReader.getId())
                                .message(Objects.nonNull(chatMessageReader.getMessage()) ? ChatMessage
                                        .builder()
                                        .id(chatMessageReader.getMessage().getId())
                                        .type(chatMessageReader.getMessage().getType())
                                        .chatRoom(Objects.nonNull(chatMessageReader.getMessage().getChatRoom())
                                                ? ChatRoom.builder()
                                                .id(chatMessageReader.getMessage().getChatRoom().getId())
                                                .build() : null)
                                        .build() : null)
                                .build()).collect(Collectors.toList()) : null)
                .build()
                : null;
    }


    @Named("customUserMobilMapping")
    static UserNew customUserMobilMapping(final UserEntity entity) {
        return Objects.nonNull(entity)
                ? UserNew
                .builder()
                .id(entity.getId())
                .name(entity.getName())
                .surname(entity.getSurname())
                .email(entity.getEmail())
                .mobile(entity.getMobile())
                .typeUser(Objects.nonNull(entity.getTypeUser()) ? entity.getTypeUser() : null)
                .build()
                : null;
    }

    @Named("customerMapping")
    default Customer customerMapping(final CustomerEntity entity) {
        return Objects.nonNull(entity)
                ? Customer.builder()
                .id(entity.getId())
                .person(Objects.nonNull(entity.getPerson()) ? Person.builder()
                        .id(entity.getPerson().getId())
                        .name(entity.getPerson().getName())
                        .surname(entity.getPerson().getSurname())
                        .title(Objects.nonNull(entity.getPerson().getTitle()) ? PersonTitle.builder().id(entity.getPerson().getTitle().getId())
                                .name(entity.getPerson().getTitle().getName()).build() : null)
                        .emails(!CollectionUtils.isEmpty(entity.getPerson().getEmails())
                                ? entity.getPerson().getEmails().stream().map(emailEntity ->
                                        Email.builder().id(emailEntity.getId()).value(emailEntity.getValue()).build())
                                .collect(Collectors.toList()) : null)
                        .identityDocuments(!CollectionUtils.isEmpty(entity.getPerson().getIdentityDocuments())
                                ? entity.getPerson().getIdentityDocuments().stream().map(identityDocumentEntity ->
                                        IdentityDocument.builder()
                                                .id(identityDocumentEntity.getId())
                                                .value(Objects.nonNull(identityDocumentEntity.getValue()) ? identityDocumentEntity.getValue() : null)
                                                .type(Objects.nonNull(identityDocumentEntity.getType()) ? IdentityDocumentType
                                                        .builder()
                                                        .id(identityDocumentEntity.getType().getId())
                                                        .name(identityDocumentEntity.getType().getName())
                                                        .build() : null)
                                                .country(Objects.nonNull(identityDocumentEntity.getCountry()) ? Country.builder()
                                                        .id(identityDocumentEntity.getCountry().getId())
                                                        .name(identityDocumentEntity.getCountry().getName())
                                                        .build() : null).build())
                                .collect(Collectors.toList()) : null)
                        .addresses(!CollectionUtils.isEmpty(entity.getPerson().getAddresses()) ?
                                entity.getPerson().getAddresses()
                                        .stream()
                                        .map(addressEntity ->
                                                Address
                                                        .builder()
                                                        .country(Objects.nonNull(addressEntity.getCountry()) ?
                                                                Country
                                                                        .builder()
                                                                        .name(addressEntity.getCountry().getName())
                                                                        .id(addressEntity.getCountry().getId()).build() : null)
                                                        .id(addressEntity.getId())
                                                        .city(addressEntity.getCity())
                                                        .complement(addressEntity.getComplement())
                                                        .province(addressEntity.getProvince())
                                                        .street(addressEntity.getStreet())
                                                        .zipCode(addressEntity.getZipCode())
                                                        .build())
                                        .collect(Collectors.toList()) : null)
                        .build() : null)
                .groupAccount(Objects.nonNull(entity.getGroupAccount())? GroupAccount.builder().id(entity.getGroupAccount().getId()).build():null)
                .membership(Objects.nonNull(entity.getMembership()) ? Membership.builder()
                        .id(entity.getMembership().getId())
                        .name(entity.getMembership().getName())
                        .priority(entity.getMembership().getPriority()).build() : null)
                .loyaltyPoints(Objects.nonNull(entity.getLoyaltyPoints()) ? entity.getLoyaltyPoints() : null)
                .build() : null;
    }

    @Named("customerModelToEntityMapping")
    default CustomerEntity customerModelToEntityMapping(final Customer model) {
        return Objects.nonNull(model)
                ? CustomerEntity.builder()
                .id(model.getId())
                .membership(Objects.nonNull(model.getMembership()) ? MembershipEntity.builder()
                        .id(model.getMembership().getId())
                        .priority(model.getMembership().getPriority()).build() : null)
                .person(Objects.nonNull(model.getPerson()) ? PersonEntity.builder().id(model.getPerson().getId())
                        .name(model.getPerson().getName())
                        .surname(model.getPerson().getSurname()).build() : null)
                .build() : null;
    }

    @Named("AssociatedMapping")
    default Associated AssociatedMapping(final AssociatedEntity entity) {
        return Objects.nonNull(entity)
                ? Associated.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .mainContact(entity.getMainContact())
                .name(entity.getName())
                .surname(entity.getSurname())
                .position(entity.getPosition())
                .groupAccount(Objects.nonNull(entity.getGroupAccount()) ?
                        GroupAccount.builder().id(entity.getGroupAccount().getId())
                                .name(entity.getGroupAccount().getName())
                                .owner(Objects.nonNull(entity.getGroupAccount().getOwner()) ?
                                        Customer
                                                .builder()
                                                .id(entity.getGroupAccount().getOwner().getId())
                                                .loyaltyPoints(entity.getGroupAccount().getOwner().getLoyaltyPoints())
                                                .membership(Objects.nonNull(entity.getGroupAccount()
                                                        .getOwner().getMembership())?
                                                        Membership.builder()
                                                                .id(entity.getGroupAccount().getOwner()
                                                                        .getMembership().getId())
                                                                .name(entity.getGroupAccount().getOwner()
                                                                        .getMembership().getName()).build():null)
                                                .build() : null).build() : null)
                .build() : null;
    }
}
