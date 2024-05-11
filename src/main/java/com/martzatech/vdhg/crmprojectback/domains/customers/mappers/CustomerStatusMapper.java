package com.martzatech.vdhg.crmprojectback.domains.customers.mappers;

import com.martzatech.vdhg.crmprojectback.domains.customers.entities.CustomerStatusEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.CustomerStatus;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerStatusMapper {

  CustomerStatus entityToModel(CustomerStatusEntity entity);

  List<CustomerStatus> entitiesToModelList(List<CustomerStatusEntity> list);

  CustomerStatusEntity modelToEntity(CustomerStatus model);

  List<CustomerStatusEntity> modelsToEntityList(List<CustomerStatus> list);
}
