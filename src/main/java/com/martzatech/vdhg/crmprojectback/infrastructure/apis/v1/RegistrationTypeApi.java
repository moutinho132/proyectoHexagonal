package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1;

import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.RegistrationTypeRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.RegistrationTypeResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface RegistrationTypeApi {

  @Operation(summary = "Save a Registration Type")
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  RegistrationTypeResponse save(@RequestBody @Valid RegistrationTypeRequest request);

  @Operation(summary = "Get all Registration Types")
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  List<RegistrationTypeResponse> findAll();

  @Operation(summary = "Get a Registration Type by its id")
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  RegistrationTypeResponse findById(@PathVariable("id") Integer id);

  @Operation(summary = "Delete a Registration Type by its id")
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  void deleteById(@PathVariable("id") Integer id);
}
