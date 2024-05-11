package com.martzatech.vdhg.crmprojectback.domains.security.mappers;

import com.martzatech.vdhg.crmprojectback.domains.customers.mappers.AssociatedMapper;
import com.martzatech.vdhg.crmprojectback.domains.customers.mappers.CustomerMapper;
import com.martzatech.vdhg.crmprojectback.domains.security.entities.UserEntity;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import com.martzatech.vdhg.crmprojectback.domains.security.models.UserNew;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                UserStatusMapper.class,
                RoleMapper.class,
                CommonNamed.class,
                CustomerMapper.class,
                AssociatedMapper.class,
        }
)
public interface UserMapper {

    @Mapping(source = "creationUser", target = "creationUser", qualifiedByName = "customUserMapping")
    @Mapping(source = "modificationUser", target = "modificationUser", qualifiedByName = "customUserMapping")
    @Mapping(source = "customer", target = "customer", qualifiedByName = "customerMapping")
    @Mapping(source = "associated", target = "associated", qualifiedByName = "AssociatedMapping")
    User entityToModel(UserEntity entity);


    UserNew entityToModelNew(UserEntity entity);

    List<User> entitiesToModelList(List<UserEntity> list);

    UserEntity modelToEntity(User model);

    UserNew modelToModelNew(User model);

    List<UserEntity> modelsToEntityList(List<User> list);


}
