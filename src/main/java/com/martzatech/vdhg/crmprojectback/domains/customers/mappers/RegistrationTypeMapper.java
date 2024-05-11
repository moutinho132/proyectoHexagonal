package com.martzatech.vdhg.crmprojectback.domains.customers.mappers;

import com.martzatech.vdhg.crmprojectback.domains.customers.entities.RegistrationTypeEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.RegistrationType;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RegistrationTypeMapper {

  RegistrationType entityToModel(RegistrationTypeEntity entity);

  List<RegistrationType> entitiesToModelList(List<RegistrationTypeEntity> list);

  RegistrationTypeEntity modelToEntity(RegistrationType model);

  List<RegistrationTypeEntity> modelsToEntityList(List<RegistrationType> list);
}
