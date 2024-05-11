package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.controllers;

import com.martzatech.vdhg.crmprojectback.domains.commons.services.LanguageService;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.LanguageApi;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.LanguageApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.LanguageResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/languages")
public class LanguageController implements LanguageApi {

  private LanguageService service;

  private LanguageApiMapper mapper;

  @Override
  public List<LanguageResponse> findAll() {
    return mapper.modelsToResponseList(service.findAll());
  }

  @Override
  public LanguageResponse findById(final Integer id) {
    return mapper.modelToResponse(service.findById(id));
  }
}
