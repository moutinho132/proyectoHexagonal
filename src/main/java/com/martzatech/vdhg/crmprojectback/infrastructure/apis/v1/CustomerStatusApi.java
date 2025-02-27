package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1;

import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.CustomerStatusRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.CustomerStatusResponse;
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

public interface CustomerStatusApi {

  @Operation(summary = "Save a Customer Status")
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  CustomerStatusResponse save(@RequestBody @Valid CustomerStatusRequest request);

  @Operation(summary = "Get all CustomerStatus")
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  List<CustomerStatusResponse> findAll();

  @Operation(summary = "Get a CustomerStatus by its id")
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  CustomerStatusResponse findById(@PathVariable("id") Integer id);

  @Operation(summary = "Delete a CustomerStatus by its id")
  @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.ACCEPTED)
  void deleteById(@PathVariable("id") Integer id);
}
