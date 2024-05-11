package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.customers.models.Corporate;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.CorporateRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.CorporateResponse;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = {
        CustomerApiMapper.class
    }
)
public interface CorporateApiMapper {

  Corporate requestToModel(CorporateRequest request);

  List<Corporate> requestToModelList(List<CorporateRequest> list);

  CorporateResponse modelToResponse(Corporate model);

  List<CorporateResponse> modelsToResponseList(List<Corporate> list);
}
