package com.martzatech.vdhg.crmprojectback.infrastructure.api.v2.controller;

import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.UnauthorizedException;
import com.martzatech.vdhg.crmprojectback.application.services.mobil.SecurityManagementAppService;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import com.martzatech.vdhg.crmprojectback.infrastructure.api.v2.AccountMobileApi;
import com.martzatech.vdhg.crmprojectback.infrastructure.api.v2.response.AccounMobileResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.UserApiMapper;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v2/account-mobile")
@Validated
@Slf4j
public class AccountMobileController implements AccountMobileApi {
    private UserApiMapper userApiMapper;

    private SecurityManagementAppService managementService;

    @Override
    public AccounMobileResponse accountMobileById(final String token){
        User user = null;
        try {
            user = managementService.findCurrentUser(token);
        }catch (ExpiredJwtException e){
            throw new UnauthorizedException(e.getMessage());
        }
        return userApiMapper.modelToAccountMobileResponse(managementService.findUserById(user.getId(),token));
    }
}
