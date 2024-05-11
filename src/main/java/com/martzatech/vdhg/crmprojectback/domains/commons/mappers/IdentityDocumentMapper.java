package com.martzatech.vdhg.crmprojectback.domains.commons.mappers;

import com.martzatech.vdhg.crmprojectback.domains.commons.entities.IdentityDocumentEntity;
import com.martzatech.vdhg.crmprojectback.domains.commons.models.IdentityDocument;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = {
        IdentityDocumentTypeMapper.class
    }
)
public interface IdentityDocumentMapper {

  IdentityDocument entityToModel(IdentityDocumentEntity entity);

  List<IdentityDocument> entitiesToModelList(List<IdentityDocumentEntity> list);

  IdentityDocumentEntity modelToEntity(IdentityDocument model);

  List<IdentityDocumentEntity> modelsToEntityList(List<IdentityDocument> list);
}
