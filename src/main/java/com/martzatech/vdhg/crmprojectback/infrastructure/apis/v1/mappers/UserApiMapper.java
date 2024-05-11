package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import com.martzatech.vdhg.crmprojectback.infrastructure.api.v2.response.AccounMobileResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.UserRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.UserMobileResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.UserResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                RoleApiMapper.class,
                CustomerApiMapper.class,
                AssociatedApiMapper.class,
        }
)
public interface UserApiMapper {

    User requestToModel(UserRequest request);

    List<User> requestToModelList(List<UserRequest> list);

    UserResponse modelToResponse(User model);

    AccounMobileResponse modelToAccountMobileResponse(User model);

    UserMobileResponse modelTotMobileResponse(User model);

    List<UserResponse> modelsToResponseList(List<User> list);
}
