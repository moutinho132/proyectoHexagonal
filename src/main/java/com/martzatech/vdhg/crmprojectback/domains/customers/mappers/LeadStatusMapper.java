package com.martzatech.vdhg.crmprojectback.domains.customers.mappers;

import com.martzatech.vdhg.crmprojectback.domains.customers.entities.LeadStatusEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.LeadStatus;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LeadStatusMapper {

  LeadStatus entityToModel(LeadStatusEntity entity);

  List<LeadStatus> entitiesToModelList(List<LeadStatusEntity> list);

  LeadStatusEntity modelToEntity(LeadStatus model);

  List<LeadStatusEntity> modelsToEntityList(List<LeadStatus> list);
}
