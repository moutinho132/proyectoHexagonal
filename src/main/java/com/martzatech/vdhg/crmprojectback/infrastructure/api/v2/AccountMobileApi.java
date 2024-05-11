package com.martzatech.vdhg.crmprojectback.infrastructure.api.v2;

import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.ErrorResponseToken;
import com.martzatech.vdhg.crmprojectback.infrastructure.api.v2.response.AccounMobileResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface AccountMobileApi {

  @Operation(summary = "Account Mobile by ID")
  @GetMapping( produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  AccounMobileResponse accountMobileById(@RequestHeader(value = "Authorization") final String token) throws ErrorResponseToken;
}
