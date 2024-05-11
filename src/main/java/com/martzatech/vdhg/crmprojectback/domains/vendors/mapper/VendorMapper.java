package com.martzatech.vdhg.crmprojectback.domains.vendors.mapper;

import com.martzatech.vdhg.crmprojectback.domains.chat.entities.FileEntity;
import com.martzatech.vdhg.crmprojectback.domains.chat.mappers.FileMapper;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.File;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.VendorProduct;
import com.martzatech.vdhg.crmprojectback.domains.security.entities.UserEntity;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import com.martzatech.vdhg.crmprojectback.domains.vendors.entities.VendorEntity;
import com.martzatech.vdhg.crmprojectback.domains.vendors.models.Vendor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
        uses = {
                VendorContactMapper.class,
                VendorLocationMapper.class,
                FileMapper.class,
        }
)
public interface VendorMapper {
    @Mapping(source = "creationUser", target = "creationUser", qualifiedByName = "customUser")
    @Mapping(source = "modificationUser", target = "modificationUser", qualifiedByName = "customUser")
    Vendor entityToModel(VendorEntity entity);

    List<Vendor> entitiesToModelList(List<VendorEntity> list);

    @Mapping(source = "creationUser", target = "creationUser", qualifiedByName = "customUserToEntity")
    VendorEntity modelToEntity(Vendor model);

    @Mapping(source = "id", target = "vendorId")
    VendorProduct modelToVendorProduct(Vendor model);

   /* @Mapping(source = "creationUser", target = "creationUser", qualifiedByName = "customUser")
    @Mapping(source = "modificationUser", target = "modificationUser", qualifiedByName = "customUser")
    @Mapping(source = "id", target = "vendorId")
    VendorProduct entityToModelVendorProduct(VendorEntity entity);*/

    List<VendorEntity> modelsToEntityList(List<Vendor> list);


    @Named("customerListFileMapping")
    static List<File> customerListFileMapping(List<FileEntity>  entities ){
        return Objects.nonNull(entities)? entities.stream()
                .map(fileEntity  -> File
                        .builder()
                        .id(fileEntity.getId())
                        .creationUser(Objects.isNull(fileEntity.getCreationUser())?null:User.builder().id(fileEntity.getCreationUser().getId()).build())
                        .creationTime(Objects.nonNull(fileEntity.getCreationTime())?fileEntity.getCreationTime():null)
                        .extension(fileEntity.getExtension())
                        .name(fileEntity.getName())
                        .url(fileEntity.getUrl())
                        .build()).collect(Collectors.toList()):null;
    }
    @Named("customUser")
    static User customUser(final UserEntity entity) {
        return Objects.nonNull(entity)
                ? User
                .builder()
                .id(entity.getId())
                .name(Objects.nonNull(entity.getName())? entity.getName() : null)
                .surname(Objects.nonNull(entity.getSurname())? entity.getSurname(): null)
                .email(Objects.nonNull(entity.getEmail())? entity.getEmail() : null)
                .typeUser(Objects.nonNull(entity.getTypeUser())? entity.getTypeUser() : null)
                .creationTime(Objects.nonNull(entity.getCreationTime())?entity.getCreationTime():null)
                .modificationTime(Objects.nonNull(entity.getModificationTime())?entity.getModificationTime():null)
                .build()
                : null;
    }

    @Named("customUserToEntity")
    static UserEntity customUserToEntity(final User model) {
        return Objects.nonNull(model)
                ? UserEntity
                .builder()
                .id(model.getId())
                .name(Objects.nonNull(model.getName())? model.getName() : null)
                .surname(Objects.nonNull(model.getSurname())? model.getSurname(): null)
                .email(Objects.nonNull(model.getEmail())? model.getEmail() : null)
                .typeUser(Objects.nonNull(model.getTypeUser())? model.getTypeUser() : null)
                .creationTime(Objects.nonNull(model.getCreationTime())?model.getCreationTime():null)
                .modificationTime(Objects.nonNull(model.getModificationTime())?model.getModificationTime():null)
                .build()
                : null;
    }

}
