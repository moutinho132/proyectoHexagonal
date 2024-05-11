package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.commons.models.Gender;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.GenderRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.GenderResponse;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GenderApiMapper {

  Gender requestToModel(GenderRequest request);

  List<Gender> requestToModelList(List<GenderRequest> list);

  GenderResponse modelToResponse(Gender model);

  List<GenderResponse> modelsToResponseList(List<Gender> list);
}
