package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.security.mappers.UserMapper;
import com.martzatech.vdhg.crmprojectback.domains.security.models.UserMobil;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.UserMobilRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.UserMobilRequestDto;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.UserMobilResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                UserMapper.class
        }
)
public interface UserMobilApiMapper {

    UserMobil requestToModel(UserMobilRequestDto request);

    List<UserMobil> requestToModelList(List<UserMobilRequest> list);

    UserMobilResponse modelToResponse(UserMobil model);

    List<UserMobilResponse> modelsToResponseList(List<UserMobil> list);
}
