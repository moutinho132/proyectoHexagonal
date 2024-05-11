package com.martzatech.vdhg.crmprojectback.infrastructure.api.v2.controller;

import com.martzatech.vdhg.crmprojectback.application.services.mobil.SecurityManagementAppService;
import com.martzatech.vdhg.crmprojectback.domains.security.mappers.UserMobilMapper;
import com.martzatech.vdhg.crmprojectback.domains.security.services.UserMobilService;
import com.martzatech.vdhg.crmprojectback.infrastructure.api.v2.SecurityMobilApi;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.UserApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.UserMobilApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.UserMobilNewApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.UserMobilRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.UserMobilRequestDto;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.UserMobilResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.UserMobileResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.UserNewResponse;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v2/security")
@Validated
public class SecurityMobilController implements SecurityMobilApi {

    private SecurityManagementAppService managementService;
    private UserMobilMapper mobilMapper;
    private UserMobilApiMapper userMobilApiMapper;
    private UserMobilService mobilService;
    private UserMobilNewApiMapper userMobilNewApiMapper;
    private UserApiMapper userApiMapper;


    @Override
    public UserNewResponse UserLogin(UserMobilRequest request) {
        return userMobilNewApiMapper.modelToResponse(managementService.validateUserLoginApp(request.getUserName()
                , request.getPassword()));
    }

    @Override
    public UserMobilResponse saveUserMobil(UserMobilRequestDto request, String token) {
        managementService.validateToken(token);
        return userMobilApiMapper.modelToResponse(managementService.saveUserMobil(mobilMapper.requestToModel(request)));
    }

    @Override
    public UserMobilResponse findById(Integer id, final String token) {
        managementService.validateToken(token);
        // return userMobilApiMapper.modelToResponse(managementService.findById(id));
        return null;
    }

    @Override
    public UserMobileResponse findCurrentUser(String token) {
        //userMobilNewApiMapper.modelToResponse(managementService.updateLastLoginAndGetCurrentUser(token));
        return  userApiMapper.modelTotMobileResponse(managementService.findCurrentUser(token));
    }
}
