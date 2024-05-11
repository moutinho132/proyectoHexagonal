package com.martzatech.vdhg.crmprojectback.domains.commons.mappers;

import com.martzatech.vdhg.crmprojectback.domains.commons.entities.CountryEntity;
import com.martzatech.vdhg.crmprojectback.domains.commons.models.Country;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CountryMapper {

  Country entityToModel(CountryEntity entity);

  List<Country> entitiesToModelList(List<CountryEntity> list);

  CountryEntity modelToEntity(Country model);

  List<CountryEntity> modelsToEntityList(List<Country> list);
}
