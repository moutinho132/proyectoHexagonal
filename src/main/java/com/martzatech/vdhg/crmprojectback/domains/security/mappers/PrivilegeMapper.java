package com.martzatech.vdhg.crmprojectback.domains.security.mappers;

import com.martzatech.vdhg.crmprojectback.domains.security.entities.PrivilegeEntity;
import com.martzatech.vdhg.crmprojectback.domains.security.models.Privilege;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = {
        PermissionMapper.class
    }
)
public interface PrivilegeMapper {

  Privilege entityToModel(PrivilegeEntity entity);

  List<Privilege> entitiesToModelList(List<PrivilegeEntity> list);

  PrivilegeEntity modelToEntity(Privilege model);

  List<PrivilegeEntity> modelsToEntityList(List<Privilege> list);
}
