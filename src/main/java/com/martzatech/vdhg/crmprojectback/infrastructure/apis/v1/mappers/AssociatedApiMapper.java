package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.customers.models.Associated;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.AssociatedRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.AssociatedResponse;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring"
)
public interface AssociatedApiMapper {

  Associated requestToModel(AssociatedRequest request);

  List<Associated> requestToModelList(List<AssociatedRequest> list);

  AssociatedResponse modelToResponse(Associated model);

  List<AssociatedResponse> modelsToResponseList(List<Associated> list);
}
