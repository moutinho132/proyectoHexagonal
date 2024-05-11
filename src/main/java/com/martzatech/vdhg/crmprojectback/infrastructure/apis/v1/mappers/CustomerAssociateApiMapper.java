package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.security.models.CustomerAssociate;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.CustomerAssociateResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring"
)
public interface CustomerAssociateApiMapper {

    CustomerAssociateResponse modelToResponse(CustomerAssociate model);

    List<CustomerAssociateResponse> modelsToResponseList(List<CustomerAssociate> list);
}
