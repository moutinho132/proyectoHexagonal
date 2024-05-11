package com.martzatech.vdhg.crmprojectback.domains.chat.mappers;

import com.martzatech.vdhg.crmprojectback.application.enums.UserTypeEnum;
import com.martzatech.vdhg.crmprojectback.domains.chat.entities.ChatRoomEntity;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.ChatRoom;
import com.martzatech.vdhg.crmprojectback.domains.commons.models.Person;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.CustomerEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.MembershipEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.mappers.CustomerMapper;
import com.martzatech.vdhg.crmprojectback.domains.customers.mappers.GroupAccountMapper;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Associated;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Customer;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Membership;
import com.martzatech.vdhg.crmprojectback.domains.security.entities.UserEntity;
import com.martzatech.vdhg.crmprojectback.domains.security.mappers.CommonNamed;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import com.martzatech.vdhg.crmprojectback.domains.security.models.UserStatus;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.UserApiMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
        uses = {
                CommonNamed.class,
                UserApiMapper.class,
                GroupAccountMapper.class,
                CustomerMapper.class,
        }
)
public interface ChatRoomMapper {
    @Mapping(source = "creationUser", target = "creationUser", qualifiedByName = "customUserMapping")
    @Mapping(source = "members", target = "members", qualifiedByName = "customMembersMapping")
    @Mapping(source = "groupAccount", target = "groupAccount", qualifiedByName = "customGroupAccountMapping")
    @Mapping(source = "customer", target = "customer", qualifiedByName = "customerMapping")
    ChatRoom entityToModel(ChatRoomEntity entity);

    @Mapping(source = "customer", target = "customer", qualifiedByName = "customerMapping")
    List<ChatRoom> entitiesToModelList(List<ChatRoomEntity> list);

    @Mapping(source = "groupAccount", target = "groupAccount", qualifiedByName = "customGroupAccountReverMapping")
    @Mapping(source = "creationUser", target = "creationUser", qualifiedByName = "customUserMapping")
    @Mapping(source = "customer", target = "customer", qualifiedByName = "customerModelToEntityMapping")
    ChatRoomEntity modelToEntity(ChatRoom model);

    @Mapping(source = "customer", target = "customer", qualifiedByName = "customerModelToEntityMapping")
    List<ChatRoomEntity> modelsToEntityList(List<ChatRoom> list);

    @Named("customMembersMapping")
    static List<User> customMembersMapping(final List<UserEntity> entities) {
        return !CollectionUtils.isEmpty(entities)
                ? entities.stream()
                .map(entity -> User
                        .builder()
                        .id(entity.getId())
                        .title(entity.getTitle())
                        .name(entity.getName())
                        .surname(entity.getSurname())
                        .status(Objects.nonNull(entity.getStatus())? UserStatus.builder().id(entity.getStatus().getId()).build():null)
                        .customer(Objects.nonNull(entity.getCustomer()) ? Customer.builder()
                                .id(entity.getCustomer().getId())
                                .membership(Objects.nonNull(entity.getCustomer().getMembership()) ?
                                        Membership.builder()
                                                .id(entity.getCustomer().getMembership().getId())
                                                .name(entity.getCustomer().getMembership().getName())
                                                .description(entity.getCustomer().getMembership().getDescription())
                                                .priority(entity.getCustomer().getMembership().getPriority()).build() : null)
                                .build() : null)
                        .associated(Objects.nonNull(entity.getAssociated()) &&
                                entity.getAssociated().equals(UserTypeEnum.ASSOCIATE.name()) ?
                                Associated
                                        .builder()
                                        .id(entity.getAssociated().getId())
                                        .name(entity.getAssociated().getName())
                                        .surname(entity.getAssociated().getSurname())
                                        .build() : null)
                        .typeUser(entity.getTypeUser())
                        .email(entity.getEmail())
                        .build())
                .collect(Collectors.toList())
                : null;
    }


    @Named("customUserMapping")
    static UserEntity customUserMapping(final User model) {
        return Objects.nonNull(model)
                ? UserEntity
                .builder()
                .id(model.getId())
                .title(model.getTitle())
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
                .typeUser(Objects.nonNull(model.getTypeUser()) ? model.getTypeUser() : null)
                .build()
                : null;
    }

    private static Customer buildCustomerMember(UserEntity entity, Optional<Membership> finalOptionalMembership) {
        return Objects.nonNull(entity.getCustomer()) ? Customer.builder()
                .id(entity.getId())
                .person(Person.builder().id(entity.getCustomer().getPerson().getId())
                        .name(entity.getCustomer().getPerson().getName())
                        .surname(entity.getCustomer().getPerson().getSurname()).build())
                .membership(finalOptionalMembership.isPresent() ? finalOptionalMembership.get() : null)
                .build() : null;
    }
}
