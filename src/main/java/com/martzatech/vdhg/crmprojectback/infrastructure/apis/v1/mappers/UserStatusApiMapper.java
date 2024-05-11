package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.security.models.UserStatus;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.UserStatusRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.UserStatusResponse;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserStatusApiMapper {

  UserStatus requestToModel(UserStatusRequest request);

  List<UserStatus> requestToModelList(List<UserStatusRequest> list);

  UserStatusResponse modelToResponse(UserStatus model);

  List<UserStatusResponse> modelsToResponseList(List<UserStatus> list);
}
