package com.martzatech.vdhg.crmprojectback.infrastructure.api.v2;

import com.martzatech.vdhg.crmprojectback.infrastructure.api.v2.response.GroupAccountMobileResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

public interface GroupAreaMobileApi {

  @Operation(summary = "Get a GroupAccount by its id mobile app")
  @GetMapping( produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  GroupAccountMobileResponse findById(@RequestHeader(value = "Authorization") final String token);

}
