package com.martzatech.vdhg.crmprojectback.domains.security.mappers;

import com.martzatech.vdhg.crmprojectback.domains.security.entities.UserStatusEntity;
import com.martzatech.vdhg.crmprojectback.domains.security.models.UserStatus;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserStatusMapper {

  UserStatus entityToModel(UserStatusEntity entity);

  List<UserStatus> entitiesToModelList(List<UserStatusEntity> list);

  UserStatusEntity modelToEntity(UserStatus model);

  List<UserStatusEntity> modelsToEntityList(List<UserStatus> list);
}
