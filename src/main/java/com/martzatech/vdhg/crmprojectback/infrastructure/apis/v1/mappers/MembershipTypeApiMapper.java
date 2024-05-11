package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.customers.models.MembershipType;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.MembershipTypeRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.MembershipTypeResponse;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MembershipTypeApiMapper {

  MembershipType requestToModel(MembershipTypeRequest request);

  List<MembershipType> requestToModelList(List<MembershipTypeRequest> list);

  MembershipTypeResponse modelToResponse(MembershipType model);

  List<MembershipTypeResponse> modelsToResponseList(List<MembershipType> list);
}
