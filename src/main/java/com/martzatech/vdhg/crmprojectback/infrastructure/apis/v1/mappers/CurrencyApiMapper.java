package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.commons.models.Currency;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.CurrencyResponse;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CurrencyApiMapper {

  CurrencyResponse modelToResponse(Currency model);

  List<CurrencyResponse> modelsToResponseList(List<Currency> list);
}
