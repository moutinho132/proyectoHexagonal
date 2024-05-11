package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.customers.models.Company;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.CompanyRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.CompanyResponse;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CompanyApiMapper {

  Company requestToModel(CompanyRequest request);

  List<Company> requestToModelList(List<CompanyRequest> list);

  CompanyResponse modelToResponse(Company model);

  List<CompanyResponse> modelsToResponseList(List<Company> list);
}
