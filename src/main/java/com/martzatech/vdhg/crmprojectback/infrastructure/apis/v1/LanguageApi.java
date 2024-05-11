package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1;

import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.LanguageResponse;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface LanguageApi {

  @Operation(summary = "Get all Languages")
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  List<LanguageResponse> findAll();

  @Operation(summary = "Get a Language by its id")
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  LanguageResponse findById(@PathVariable("id") Integer id);
}
