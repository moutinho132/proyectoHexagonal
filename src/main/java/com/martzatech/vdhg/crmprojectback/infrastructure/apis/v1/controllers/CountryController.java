package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.controllers;

import com.martzatech.vdhg.crmprojectback.domains.commons.services.CountryService;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.CountryApi;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.CountryApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.CountryResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/countries")
public class CountryController implements CountryApi {

  private CountryService service;

  private CountryApiMapper mapper;

  @Override
  public List<CountryResponse> findAll() {
    return mapper.modelsToResponseList(service.findAll());
  }

  @Override
  public CountryResponse findById(final Integer id) {
    return mapper.modelToResponse(service.findById(id));
  }
}
