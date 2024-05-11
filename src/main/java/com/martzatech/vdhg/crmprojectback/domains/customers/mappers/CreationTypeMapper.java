package com.martzatech.vdhg.crmprojectback.domains.customers.mappers;

import com.martzatech.vdhg.crmprojectback.domains.customers.entities.CreationTypeEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.CreationType;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring"
)
public interface CreationTypeMapper {

  CreationType entityToModel(CreationTypeEntity entity);

  List<CreationType> entitiesToModelList(List<CreationTypeEntity> list);

  CreationTypeEntity modelToEntity(CreationType model);

  List<CreationTypeEntity> modelsToEntityList(List<CreationType> list);
}
