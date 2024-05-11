package com.martzatech.vdhg.crmprojectback.domains.commons.mappers;

import com.martzatech.vdhg.crmprojectback.domains.commons.entities.GenderEntity;
import com.martzatech.vdhg.crmprojectback.domains.commons.models.Gender;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GenderMapper {

  Gender entityToModel(GenderEntity entity);

  List<Gender> entitiesToModelList(List<GenderEntity> list);

  GenderEntity modelToEntity(Gender model);

  List<GenderEntity> modelsToEntityList(List<Gender> list);
}
