package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.customers.models.Membership;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.MembershipRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.MembershipResponse;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = {
        MembershipTypeApiMapper.class
    }
)
public interface MembershipApiMapper {

  Membership requestToModel(MembershipRequest request);

  List<Membership> requestToModelList(List<MembershipRequest> list);

  MembershipResponse modelToResponse(Membership model);

  List<MembershipResponse> modelsToResponseList(List<Membership> list);
}
