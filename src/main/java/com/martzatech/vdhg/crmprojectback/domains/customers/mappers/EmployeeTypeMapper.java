package com.martzatech.vdhg.crmprojectback.domains.customers.mappers;

import com.martzatech.vdhg.crmprojectback.domains.customers.entities.EmployeeTypeEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.EmployeeType;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmployeeTypeMapper {

  EmployeeType entityToModel(EmployeeTypeEntity entity);

  List<EmployeeType> entitiesToModelList(List<EmployeeTypeEntity> list);

  EmployeeTypeEntity modelToEntity(EmployeeType model);

  List<EmployeeTypeEntity> modelsToEntityList(List<EmployeeType> list);
}
