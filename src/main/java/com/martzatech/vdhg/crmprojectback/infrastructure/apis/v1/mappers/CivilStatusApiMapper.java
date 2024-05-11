package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.commons.models.CivilStatus;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.CivilStatusRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.CivilStatusResponse;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CivilStatusApiMapper {

  CivilStatus requestToModel(CivilStatusRequest request);

  List<CivilStatus> requestToModelList(List<CivilStatusRequest> list);

  CivilStatusResponse modelToResponse(CivilStatus model);

  List<CivilStatusResponse> modelsToResponseList(List<CivilStatus> list);
}
