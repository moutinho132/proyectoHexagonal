package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.security.models.Subsidiary;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.SubsidiaryRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.SubsidiaryResponse;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubsidiaryApiMapper {

  Subsidiary requestToModel(SubsidiaryRequest request);

  List<Subsidiary> requestToModelList(List<SubsidiaryRequest> list);

  SubsidiaryResponse modelToResponse(Subsidiary model);

  List<SubsidiaryResponse> modelsToResponseList(List<Subsidiary> list);
}
