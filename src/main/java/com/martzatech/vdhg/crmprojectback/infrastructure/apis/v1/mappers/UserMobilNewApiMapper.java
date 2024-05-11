package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.security.models.UserMobil;
import com.martzatech.vdhg.crmprojectback.domains.security.models.UserNew;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.UserMobilRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.UserMobilRequestDto;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.UserMobilResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.UserNewResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring"

)
public interface UserMobilNewApiMapper {

    UserMobil requestToModel(UserMobilRequestDto request);

    List<UserMobil> requestToModelList(List<UserMobilRequest> list);

    UserNewResponse modelToResponse(UserNew model);

    List<UserMobilResponse> modelsToResponseList(List<UserMobil> list);
}
