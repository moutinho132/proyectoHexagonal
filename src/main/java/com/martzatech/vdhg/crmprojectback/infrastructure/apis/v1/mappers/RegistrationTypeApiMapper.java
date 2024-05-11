package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.customers.models.RegistrationType;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.RegistrationTypeRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.RegistrationTypeResponse;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RegistrationTypeApiMapper {

  RegistrationType requestToModel(RegistrationTypeRequest request);

  List<RegistrationType> requestToModelList(List<RegistrationTypeRequest> list);

  RegistrationTypeResponse modelToResponse(RegistrationType model);

  List<RegistrationTypeResponse> modelsToResponseList(List<RegistrationType> list);
}
