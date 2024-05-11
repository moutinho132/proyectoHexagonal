package com.martzatech.vdhg.crmprojectback.domains.commons.mappers;

import com.martzatech.vdhg.crmprojectback.domains.commons.entities.AttributeTypeEntity;
import com.martzatech.vdhg.crmprojectback.domains.commons.models.AttributeType;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AttributeTypeMapper {

  AttributeType entityToModel(AttributeTypeEntity entity);

  List<AttributeType> entitiesToModelList(List<AttributeTypeEntity> list);

  AttributeTypeEntity modelToEntity(AttributeType model);

  List<AttributeTypeEntity> modelsToEntityList(List<AttributeType> list);
}
