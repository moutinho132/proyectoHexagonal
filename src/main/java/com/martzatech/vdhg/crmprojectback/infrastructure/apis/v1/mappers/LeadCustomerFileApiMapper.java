package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.customers.models.LeadCustomerFile;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.LeadCustomerFileResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
    componentModel = "spring"
)
public interface LeadCustomerFileApiMapper {

  LeadCustomerFileResponse modelToResponse(LeadCustomerFile model);

  List<LeadCustomerFileResponse> modelsToResponseList(List<LeadCustomerFile> list);
}
