package com.martzatech.vdhg.crmprojectback.domains.commons.mappers;

import com.martzatech.vdhg.crmprojectback.domains.commons.entities.LanguageEntity;
import com.martzatech.vdhg.crmprojectback.domains.commons.models.Language;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LanguageMapper {

  Language entityToModel(LanguageEntity entity);

  List<Language> entitiesToModelList(List<LanguageEntity> list);

  LanguageEntity modelToEntity(Language model);

  List<LanguageEntity> modelsToEntityList(List<Language> list);
}
