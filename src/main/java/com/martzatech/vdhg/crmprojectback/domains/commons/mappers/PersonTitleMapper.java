package com.martzatech.vdhg.crmprojectback.domains.commons.mappers;

import com.martzatech.vdhg.crmprojectback.domains.commons.entities.PersonTitleEntity;
import com.martzatech.vdhg.crmprojectback.domains.commons.models.PersonTitle;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonTitleMapper {

  PersonTitle entityToModel(PersonTitleEntity entity);

  List<PersonTitle> entitiesToModelList(List<PersonTitleEntity> list);

  PersonTitleEntity modelToEntity(PersonTitle model);

  List<PersonTitleEntity> modelsToEntityList(List<PersonTitle> list);
}
