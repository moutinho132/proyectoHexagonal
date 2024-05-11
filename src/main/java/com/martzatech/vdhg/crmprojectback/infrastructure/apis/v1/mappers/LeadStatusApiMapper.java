package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.customers.models.LeadStatus;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.LeadStatusRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.LeadStatusResponse;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LeadStatusApiMapper {

  LeadStatus requestToModel(LeadStatusRequest request);

  List<LeadStatus> requestToModelList(List<LeadStatusRequest> list);

  LeadStatusResponse modelToResponse(LeadStatus model);

  List<LeadStatusResponse> modelsToResponseList(List<LeadStatus> list);
}
