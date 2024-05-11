package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.customers.models.Lead;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.LeadRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.LeadResponse;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = {
        LeadStatusApiMapper.class,
        CompanyApiMapper.class,
        RegistrationTypeApiMapper.class,
        PersonApiMapper.class
    }
)
public interface LeadApiMapper {

  @Mapping(source = "referringCustomer", target = "referringCustomer", ignore = true)
  Lead requestToModel(LeadRequest request);

  List<Lead> requestToModelList(List<LeadRequest> list);

  @Mapping(source = "referringCustomer", target = "referringCustomer", ignore = true)
  LeadResponse modelToResponse(Lead model);

  List<LeadResponse> modelsToResponseList(List<Lead> list);
}
