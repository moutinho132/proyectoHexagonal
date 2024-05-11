package com.martzatech.vdhg.crmprojectback.domains.security.mappers;

import com.martzatech.vdhg.crmprojectback.domains.security.entities.SubsidiaryEntity;
import com.martzatech.vdhg.crmprojectback.domains.security.models.Subsidiary;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubsidiaryMapper {

  Subsidiary entityToModel(SubsidiaryEntity entity);

  List<Subsidiary> entitiesToModelList(List<SubsidiaryEntity> list);

  SubsidiaryEntity modelToEntity(Subsidiary model);

  List<SubsidiaryEntity> modelsToEntityList(List<Subsidiary> list);
}
