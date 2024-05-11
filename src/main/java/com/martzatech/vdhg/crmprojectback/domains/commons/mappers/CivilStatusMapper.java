package com.martzatech.vdhg.crmprojectback.domains.commons.mappers;

import com.martzatech.vdhg.crmprojectback.domains.commons.entities.CivilStatusEntity;
import com.martzatech.vdhg.crmprojectback.domains.commons.models.CivilStatus;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CivilStatusMapper {

  CivilStatus entityToModel(CivilStatusEntity entity);

  List<CivilStatus> entitiesToModelList(List<CivilStatusEntity> list);

  CivilStatusEntity modelToEntity(CivilStatus model);

  List<CivilStatusEntity> modelsToEntityList(List<CivilStatus> list);
}
