package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.customers.models.CustomerStatus;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.CustomerStatusRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.CustomerStatusResponse;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerStatusApiMapper {

  CustomerStatus requestToModel(CustomerStatusRequest request);

  List<CustomerStatus> requestToModelList(List<CustomerStatusRequest> list);

  CustomerStatusResponse modelToResponse(CustomerStatus model);

  List<CustomerStatusResponse> modelsToResponseList(List<CustomerStatus> list);
}
