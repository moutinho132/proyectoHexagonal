package com.martzatech.vdhg.crmprojectback.domains.chat.mappers;

import com.martzatech.vdhg.crmprojectback.domains.chat.entities.FileEntity;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.File;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.FileOffer;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.FileWallet;
import com.martzatech.vdhg.crmprojectback.domains.commons.mappers.PersonMapper;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.CustomerEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.MembershipEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.mappers.OfferMapper;
import com.martzatech.vdhg.crmprojectback.domains.customers.mappers.OrderMapper;
import com.martzatech.vdhg.crmprojectback.domains.customers.mappers.PreOfferMapper;
import com.martzatech.vdhg.crmprojectback.domains.customers.mappers.ProductMapper;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Customer;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Membership;
import com.martzatech.vdhg.crmprojectback.domains.security.entities.UserEntity;
import com.martzatech.vdhg.crmprojectback.domains.security.mappers.CommonNamed;
import com.martzatech.vdhg.crmprojectback.domains.security.mappers.UserMapper;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.FileResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.FileWalletResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.UserWalletResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Objects;

@Mapper(
        componentModel = "spring",
        uses = {
                UserMapper.class,
                ChatMessageMapper.class,
                CommonNamed.class,
                OrderMapper.class,
                ProductMapper.class,
                OfferMapper.class,
                PersonMapper.class,
                PreOfferMapper.class
        }

)
public interface FileMapper {
    @Mapping(source = "creationUser", target = "creationUser", qualifiedByName = "customsUserMapping")
    @Mapping(source = "removalUser", target = "removalUser", qualifiedByName = "customsUserMapping")
    @Mapping(source = "persons", target = "persons", ignore = true)
    @Mapping(source = "products", target = "products", ignore = true)
    @Mapping(source = "status", target = "status",ignore = true)
    File entityToModel(FileEntity entity);

    @Mapping(source = "creationUser", target = "creationUser", qualifiedByName = "customsUserMapping")
   // @Mapping(source = "removalUser", target = "removalUser", qualifiedByName = "customsUserMapping")
    FileResponse entityToModelResponse(FileEntity entity);


    //@Mapping(source = "creationUser", target = "creationUser", qualifiedByName = "customsUserWalletMapping")
    FileWalletResponse entityToModelWalletResponse(FileEntity entity);

    FileWallet modelToModel(File model);


    @Mapping(source = "creationUser", target = "creationUser", qualifiedByName = "customsUserMapping")
    FileOffer entityToModelOffer(FileEntity entity);

    @Mapping(source = "creationUser", target = "creationUser", qualifiedByName = "customsUserMapping")
    List<FileOffer> entitiesToModelListOffer(List<FileEntity> list);

    @Mapping(source = "creationUser", target = "creationUser", qualifiedByName = "customUserMapping")
    @Mapping(source = "removalUser", target = "removalUser", qualifiedByName = "customUserMapping")
    @Mapping(source = "offers", target = "offers", ignore = true)
    @Mapping(source = "persons", target = "persons", ignore = true)
    @Mapping(source = "products", target = "products", ignore = true)
    @Mapping(source = "orders", target = "orders",ignore = true)
    @Mapping(source = "status", target = "status",ignore = true)
    List<File> entitiesToModelList(List<FileEntity> list);

    @Mapping(source = "creationUser", target = "creationUser", qualifiedByName = "customUserEntityMapping")
    @Mapping(source = "removalUser", target = "removalUser", qualifiedByName = "customUserEntityMapping")
    FileEntity modelToEntity(File model);

    @Mapping(source = "creationUser", target = "creationUser", qualifiedByName = "customUserEntityMapping")
    FileEntity modelResponseToEntity(FileResponse model);

    List<FileEntity> modelsToEntityList(List<File> list);

    @Named("customUserEntityMapping")
    static UserEntity customUserMapping(final User model) {
        return Objects.nonNull(model)
                ? UserEntity
                .builder()
                .id(model.getId())
                .title(Objects.nonNull(model.getTitle()) ? model.getTitle() : null)
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

    @Named("customsUserMapping")
    static User customsUserMapping(final UserEntity entity) {
        return Objects.nonNull(entity)
                ? User
                .builder()
                .id(entity.getId())
                //.title(StringUtils.isNotBlank(entity.getTitle())?entity.getTitle():null)
                .name(entity.getName())
                .surname(entity.getSurname())
                .email(entity.getEmail())
                .address(entity.getAddress())
                .mobile(entity.getMobile())
                .customer(Objects.nonNull(entity.getCustomer()) ? Customer.builder()
                        .id(entity.getCustomer().getId())
                        .membership(Membership.builder()
                                .id(entity.getCustomer().getMembership().getId())
                                .priority(entity.getCustomer().getMembership().getPriority()).build())
                        .build() : null)
                .typeUser(Objects.nonNull(entity.getTypeUser()) ? entity.getTypeUser() : null)
                .build()
                : null;
    }

    @Named("customsUserWalletMapping")
    static UserWalletResponse customsUserWalletMapping(final UserEntity entity) {
        return Objects.nonNull(entity)
                ? UserWalletResponse
                .builder()
                .id(entity.getId())
                //.title(StringUtils.isNotBlank(entity.getTitle())?entity.getTitle():null)
                .name(entity.getName())
                .surname(entity.getSurname())
                .build()
                : null;
    }

}
