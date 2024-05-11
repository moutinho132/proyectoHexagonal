package com.martzatech.vdhg.crmprojectback.domains.security.mappers;

import com.martzatech.vdhg.crmprojectback.domains.security.entities.AccessGroupEntity;
import com.martzatech.vdhg.crmprojectback.domains.security.models.AccessGroup;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = {
        AccessMapper.class,
    }
)
public interface AccessGroupMapper {

  AccessGroup entityToModel(AccessGroupEntity entity);

  List<AccessGroup> entitiesToModelList(List<AccessGroupEntity> list);

  AccessGroupEntity modelToEntity(AccessGroup model);

  List<AccessGroupEntity> modelsToEntityList(List<AccessGroup> list);
}
