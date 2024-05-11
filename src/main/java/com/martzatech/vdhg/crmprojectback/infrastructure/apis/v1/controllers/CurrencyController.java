package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.controllers;

import com.martzatech.vdhg.crmprojectback.domains.commons.services.CurrencyService;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.CurrencyApi;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.CurrencyApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.CurrencyResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/currencies")
public class CurrencyController implements CurrencyApi {

  private CurrencyService service;

  private CurrencyApiMapper mapper;

  @Override
  public List<CurrencyResponse> findAll() {
    return mapper.modelsToResponseList(service.findAll());
  }
}
