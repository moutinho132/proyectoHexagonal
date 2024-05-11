package com.martzatech.vdhg.crmprojectback.domains.customers.mappers;

import com.martzatech.vdhg.crmprojectback.domains.chat.entities.FileEntity;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.File;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.OrderEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Order;
import com.martzatech.vdhg.crmprojectback.domains.security.entities.UserEntity;
import com.martzatech.vdhg.crmprojectback.domains.security.mappers.UserMapper;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
        uses = {
                //CustomNamedMappers.class,
                OfferMapper.class,
                PaymentDetailsMapper.class,
                UserMapper.class,
        }
)
public interface OrderMapper {

    @Mapping(source = "creationUser", target = "creationUser", ignore = true)
    @Mapping(source = "modificationUser", target = "modificationUser", ignore = true)
        //@Mapping(source = "files", target = "files", qualifiedByName = "customerListFileMapping")
    Order entityToModel(OrderEntity entity);


    List<Order> entitiesToModelList(List<OrderEntity> list);

    OrderEntity modelToEntity(Order model);

    List<OrderEntity> modelsToEntityList(List<Order> list);

    @Named("customUser")
    static User customUser(final UserEntity entity) {
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
    @Named("customerListFileMapping")
    static List<File> customerListFileMapping(List<FileEntity> entities) {
        return Objects.nonNull(entities) ? entities.stream()
                .map(fileEntity -> File
                        .builder()
                        .id(fileEntity.getId())
                        .creationUser(Objects.isNull(fileEntity.getCreationUser()) ? null : User.builder().id(fileEntity.getCreationUser().getId()).build())
                        .creationTime(Objects.nonNull(fileEntity.getCreationTime()) ? fileEntity.getCreationTime() : null)
                        .extension(fileEntity.getExtension())
                        .name(fileEntity.getName())
                        .url(fileEntity.getUrl())
                        .build()).collect(Collectors.toList()) : null;
    }
}
