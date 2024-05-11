package com.martzatech.vdhg.crmprojectback.infrastructure.api.v2;

import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.UserMobilRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.UserMobilRequestDto;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.UserMobilResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.UserMobileResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.UserNewResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

public interface SecurityMobilApi {


    //TODO: Mobil
    @Operation(summary = "User mobil login")
    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    UserNewResponse UserLogin(@RequestBody @Valid UserMobilRequest request);

    @Operation(summary = "Save User mobil")
    @PostMapping(path = "/users/app/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    UserMobilResponse saveUserMobil(@RequestBody @Valid UserMobilRequestDto request, @RequestHeader(value = "Authorization") String token);

    @Operation(summary = "User mobil")
    @GetMapping(path = "/users/app/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    UserMobilResponse findById(@PathVariable("id") Integer id, @RequestHeader(value = "Authorization") String token);

    @Operation(summary = "Get the current user")
    @GetMapping(value = "/users/current", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    UserMobileResponse findCurrentUser(@RequestHeader(value = "Authorization") String token);
}
