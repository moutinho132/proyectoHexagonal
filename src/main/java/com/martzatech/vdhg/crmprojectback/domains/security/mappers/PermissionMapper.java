package com.martzatech.vdhg.crmprojectback.domains.security.mappers;

import com.martzatech.vdhg.crmprojectback.domains.security.entities.PermissionEntity;
import com.martzatech.vdhg.crmprojectback.domains.security.models.Permission;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

  Permission entityToModel(PermissionEntity entity);

  List<Permission> entitiesToModelList(List<PermissionEntity> list);

  PermissionEntity modelToEntity(Permission model);

  List<PermissionEntity> modelsToEntityList(List<Permission> list);
}
