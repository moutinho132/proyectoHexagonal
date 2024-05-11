package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.customers.models.GroupAccount;
import com.martzatech.vdhg.crmprojectback.infrastructure.api.v2.response.GroupAccountMobileResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.GroupAccountRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.GroupAccountResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
    componentModel = "spring",
    uses = {
        CustomerApiMapper.class,
        AssociatedApiMapper.class,
        UserApiMapper.class,
    }
)
public interface GroupAccountApiMapper {

  GroupAccount requestToModel(GroupAccountRequest request);

  List<GroupAccount> requestToModelList(List<GroupAccountRequest> list);

  GroupAccountResponse modelToResponse(GroupAccount model);

  GroupAccountMobileResponse modelToMobileResponse(GroupAccount model);


    List<GroupAccountResponse> modelsToResponseList(List<GroupAccount> list);
}
