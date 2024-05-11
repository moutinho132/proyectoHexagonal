package com.martzatech.vdhg.crmprojectback.domains.commons.mappers;

import com.martzatech.vdhg.crmprojectback.domains.commons.entities.IdentityDocumentTypeEntity;
import com.martzatech.vdhg.crmprojectback.domains.commons.models.IdentityDocumentType;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IdentityDocumentTypeMapper {

  IdentityDocumentType entityToModel(IdentityDocumentTypeEntity entity);

  List<IdentityDocumentType> entitiesToModelList(List<IdentityDocumentTypeEntity> list);

  IdentityDocumentTypeEntity modelToEntity(IdentityDocumentType model);

  List<IdentityDocumentTypeEntity> modelsToEntityList(List<IdentityDocumentType> list);
}
