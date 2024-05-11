package com.martzatech.vdhg.crmprojectback.domains.customers.mappers;

import com.martzatech.vdhg.crmprojectback.domains.customers.entities.CompanyEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Company;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

  Company entityToModel(CompanyEntity entity);

  List<Company> entitiesToModelList(List<CompanyEntity> list);

  CompanyEntity modelToEntity(Company model);

  List<CompanyEntity> modelsToEntityList(List<Company> list);
}
