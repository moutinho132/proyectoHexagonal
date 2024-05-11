package com.martzatech.vdhg.crmprojectback.domains.customers.mappers;

import com.martzatech.vdhg.crmprojectback.domains.commons.entities.IdentityDocumentEntity;
import com.martzatech.vdhg.crmprojectback.domains.commons.entities.LanguageEntity;
import com.martzatech.vdhg.crmprojectback.domains.commons.entities.PersonEntity;
import com.martzatech.vdhg.crmprojectback.domains.commons.models.IdentityDocument;
import com.martzatech.vdhg.crmprojectback.domains.commons.models.Person;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.AssociatedEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.CustomerEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.GroupAccountEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Associated;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Customer;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.GroupAccount;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Membership;
import com.martzatech.vdhg.crmprojectback.domains.security.entities.UserEntity;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import org.mapstruct.Named;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public interface CustomNamedMappers {

    @Named("customGroupAccountModel")
    static GroupAccount customGroupAccountModel(final GroupAccountEntity entity) {
        return Objects.nonNull(entity)
                ? GroupAccount
                .builder()
                .id(entity.getId())
                .name(entity.getName())
                .owner(Objects.nonNull(entity.getOwner())?
                        Customer.builder().id(entity.getOwner().getId())
                                .membership(Objects.nonNull(entity.getOwner().getMembership())? Membership.builder()
                                        .id(entity.getOwner().getMembership().getId())
                                        .name(entity.getOwner().getMembership().getName())
                                        .build():null)
                                .loyaltyPoints(Objects.nonNull(entity.getOwner().getLoyaltyPoints())?entity.getOwner()
                                        .getLoyaltyPoints():null)
                                .person(Objects.nonNull(entity.getOwner().getPerson())?
                                        Person
                                                .builder()
                                                .id(entity
                                                        .getOwner().getPerson().getId())
                                                .identityDocuments(!CollectionUtils.isEmpty(entity
                                                        .getOwner().getPerson().getIdentityDocuments()) ?
                                                        entity.getOwner().getPerson().getIdentityDocuments().stream()
                                                                .map(identityDocumentEntity ->
                                                                        IdentityDocument.builder().id(identityDocumentEntity.getId())
                                                                                .value(identityDocumentEntity.getValue()).build()).collect(Collectors.toList()):null).build():null)
                                .build():null)
                .email(Objects.nonNull(entity.getEmail())? entity.getEmail() : null)
                .creationTime(Objects.nonNull(entity.getCreationTime())?entity.getCreationTime():null)
                .modificationTime(Objects.nonNull(entity.getModificationTime())?entity.getModificationTime():null)
                .build()
                : null;
    }

    @Named("customUser")
    static User customDepartmentMapping(final UserEntity entity) {
        return Objects.nonNull(entity)
                ? User
                .builder()
                .id(entity.getId())
                .title(Objects.nonNull(entity.getTitle()) ? entity.getTitle() : null )
                .name(Objects.nonNull(entity.getName())? entity.getName() : null)
                .surname(Objects.nonNull(entity.getSurname())? entity.getSurname(): null)
                .mobile(Objects.nonNull(entity.getMobile()) ? entity.getMobile():null)
                .nationality(Objects.nonNull(entity.getNationality())? entity.getNationality():null)
                .address(Objects.nonNull(entity.getAddress())? entity.getAddress() : null)
                .email(Objects.nonNull(entity.getEmail())? entity.getEmail() : null)
                .creationTime(Objects.nonNull(entity.getCreationTime())?entity.getCreationTime():null)
                .modificationTime(Objects.nonNull(entity.getModificationTime())?entity.getModificationTime():null)
                .build()
                : null;
    }

    @Named("customGroupAccount")
    static GroupAccountEntity customGroupAccount(GroupAccount account) {
        return Objects.nonNull(account)
                ? GroupAccountEntity
                .builder()
                .id(account.getId())
                .owner(Objects.nonNull(account.getOwner()) ?
                        CustomerEntity.builder()
                                .id(account.getOwner()
                                        .getId())
                                        .person(Objects.nonNull(account.getOwner().getPerson())?PersonEntity.builder().id(account.getOwner().getPerson().getId())
                                        .name(account.getOwner().getPerson().getName())
                                        .surname(account.getOwner().getPerson().getSurname())
                                        .identityDocuments(account.getOwner()
                                                .getPerson().getIdentityDocuments()
                                                .stream().map(identityDocument -> IdentityDocumentEntity.builder()
                                                        .id(identityDocument.getId())
                                                        .value(identityDocument.getValue()).build()).collect(Collectors.toList()))
                                        .build():null).build() : null)
                .build()
                : null;
    }

    @Named("customAssociates")
    static List<AssociatedEntity> customAssociates(List<Associated> associatedList) {
        return Objects.nonNull(associatedList)
                ? associatedList.stream().map(associated ->
                AssociatedEntity.builder()
                        .id(associated.getId())
                        .name(associated.getName())
                        .surname(associated.getSurname())
                        .email(associated.getEmail())
                        .position(associated.getPosition())
                        .mainContact(associated.getMainContact())
                        .phonePrefix(associated.getPhonePrefix())
                        .preferredLanguage(Objects.nonNull(associated.getPreferredLanguage())?LanguageEntity.builder()
                                .id(associated.getPreferredLanguage().getId())
                                .name(associated.getPreferredLanguage().getName())
                                .build():null)
                        .phoneNumber(associated.getPhoneNumber())
                        .creationUser(UserEntity
                                .builder()
                                .id(associated.getCreationUser().getId())
                                .title(associated.getCreationUser().getTitle())
                                .name(associated.getCreationUser().getName())
                                .surname(associated.getCreationUser().getSurname())
                                .email(associated.getCreationUser().getEmail())
                                .mobile(associated.getCreationUser().getMobile())
                                .address(associated.getCreationUser().getAddress())
                                .nationality(associated.getCreationUser().getNationality())
                                .creationTime(associated.getCreationUser().getCreationTime())
                                .modificationTime(associated.getCreationUser().getModificationTime())
                                .build())
                        .modificationUser(Objects.nonNull(associated.getModificationUser())
                                        && Objects.nonNull(associated.getModificationUser().getId()) ?
                                UserEntity
                                .builder()
                                .id(associated.getModificationUser().getId())
                                .title(associated.getModificationUser().getTitle())
                                .name(associated.getModificationUser().getName())
                                .surname(associated.getModificationUser().getSurname())
                                .email(associated.getModificationUser().getEmail())
                                .mobile(associated.getModificationUser().getMobile())
                                .address(associated.getModificationUser().getAddress())
                                .nationality(associated.getModificationUser().getNationality())
                                .creationTime(associated.getModificationUser().getCreationTime())
                                .modificationTime(associated.getModificationUser().getModificationTime())
                                .build():null)
                        .creationTime(associated.getCreationTime())
                        .modificationTime(associated.getModificationTime())
                        .build()).collect(Collectors.toList())
                : new ArrayList<>();
    }

    @Named("customAssociatesRevers")
    static List<AssociatedEntity> customAssociatesRevers(List<AssociatedEntity> entities) {
        return Objects.nonNull(entities)
                ? entities.stream().map(associated ->
                AssociatedEntity.builder()
                        .id(associated.getId())
                        .name(associated.getName())
                        .surname(associated.getSurname())
                        .email(associated.getEmail())
                        .position(associated.getPosition())
                        .mainContact(associated.getMainContact())
                        .phonePrefix(associated.getPhonePrefix())
                        .preferredLanguage(Objects.nonNull(associated.getPreferredLanguage()) ? LanguageEntity.builder()
                                .id(associated.getPreferredLanguage().getId())
                                .name(associated.getPreferredLanguage().getName())
                                .build() : null)
                        .phoneNumber(associated.getPhoneNumber())
                        .creationUser(Objects.nonNull(associated.getCreationUser()) ? UserEntity
                                .builder()
                                .id(associated.getCreationUser().getId())
                                .title(associated.getCreationUser().getTitle())
                                .name(associated.getCreationUser().getName())
                                .surname(associated.getCreationUser().getSurname())
                                .email(associated.getCreationUser().getEmail())
                                .mobile(associated.getCreationUser().getMobile())
                                .address(associated.getCreationUser().getAddress())
                                .nationality(associated.getCreationUser().getNationality())
                                .creationTime(associated.getCreationUser().getCreationTime())
                                .modificationTime(associated.getCreationUser().getModificationTime())
                                .build() : null)
                        .modificationUser(Objects.nonNull(associated.getModificationUser()) ? UserEntity
                                .builder()
                                .id(associated.getModificationUser().getId())
                                .title(associated.getModificationUser().getTitle())
                                .name(associated.getModificationUser().getName())
                                .surname(associated.getModificationUser().getSurname())
                                .email(associated.getModificationUser().getEmail())
                                .mobile(associated.getModificationUser().getMobile())
                                .address(associated.getModificationUser().getAddress())
                                .nationality(associated.getModificationUser().getNationality())
                                .creationTime(associated.getModificationUser().getCreationTime())
                                .modificationTime(associated.getModificationUser().getModificationTime())
                                .build() : null)
                        .creationTime(associated.getCreationTime())
                        .modificationTime(associated.getModificationTime())
                        .build()).collect(Collectors.toList())
                : new ArrayList<>();
    }


}
