package com.martzatech.vdhg.crmprojectback.domains.commons.mappers;

import com.martzatech.vdhg.crmprojectback.domains.commons.entities.CurrencyEntity;
import com.martzatech.vdhg.crmprojectback.domains.commons.models.Currency;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {

  Currency entityToModel(CurrencyEntity entity);

  List<Currency> entitiesToModelList(List<CurrencyEntity> list);
}
