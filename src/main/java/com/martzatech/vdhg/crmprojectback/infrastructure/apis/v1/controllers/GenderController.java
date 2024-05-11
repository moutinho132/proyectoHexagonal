package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.controllers;

import com.martzatech.vdhg.crmprojectback.domains.commons.services.GenderService;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.GenderApi;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.GenderApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.GenderRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.GenderResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/genders")
@Validated
public class GenderController implements GenderApi {

  private GenderService service;

  private GenderApiMapper mapper;

  @Override
  public GenderResponse save(final GenderRequest request) {
    return mapper.modelToResponse(service.save(mapper.requestToModel(request)));
  }

  @Override
  public List<GenderResponse> findAll() {
    return mapper.modelsToResponseList(service.findAll());
  }

  @Override
  public GenderResponse findById(final Integer id) {
    return mapper.modelToResponse(service.findById(id));
  }

  @Override
  public void deleteById(final Integer id) {
    service.deleteById(id);
  }
}
