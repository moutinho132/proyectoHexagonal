package com.martzatech.vdhg.crmprojectback.domains.customers.mappers;

import com.martzatech.vdhg.crmprojectback.domains.customers.entities.CorporateEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Corporate;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = {
        CustomerMapper.class,
        CustomNamedMappers.class
    }
)
public interface CorporateMapper {

  @Mapping(source = "creationUser", target = "creationUser", qualifiedByName = "customUser")
  @Mapping(source = "modificationUser", target = "modificationUser", qualifiedByName = "customUser")
  Corporate entityToModel(CorporateEntity entity);

  List<Corporate> entitiesToModelList(List<CorporateEntity> list);

  CorporateEntity modelToEntity(Corporate model);

  List<CorporateEntity> modelsToEntityList(List<Corporate> list);
}
