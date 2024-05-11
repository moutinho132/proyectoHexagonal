package com.martzatech.vdhg.crmprojectback.domains.security.mappers;

import com.martzatech.vdhg.crmprojectback.domains.security.entities.AccessEntity;
import com.martzatech.vdhg.crmprojectback.domains.security.models.Access;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = {
        PrivilegeMapper.class,
    }
)
public interface AccessMapper {

  Access entityToModel(AccessEntity entity);

  List<Access> entitiesToModelList(List<AccessEntity> list);

  AccessEntity modelToEntity(Access model);

  List<AccessEntity> modelsToEntityList(List<Access> list);
}
