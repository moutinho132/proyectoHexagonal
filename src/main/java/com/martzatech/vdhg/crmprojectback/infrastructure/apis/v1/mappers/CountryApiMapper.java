package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.commons.models.Country;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.CountryRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.CountryResponse;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CountryApiMapper {

  Country requestToModel(CountryRequest request);

  List<Country> requestToModelList(List<CountryRequest> list);

  CountryResponse modelToResponse(Country model);

  List<CountryResponse> modelsToResponseList(List<Country> list);
}
