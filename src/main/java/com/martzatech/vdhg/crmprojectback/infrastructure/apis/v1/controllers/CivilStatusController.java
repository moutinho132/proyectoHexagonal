package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.controllers;

import com.martzatech.vdhg.crmprojectback.domains.commons.services.CivilStatusService;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.CivilStatusApi;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.CivilStatusApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.CivilStatusRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.CivilStatusResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/civil-status")
@Validated
public class CivilStatusController implements CivilStatusApi {

  private CivilStatusService service;

  private CivilStatusApiMapper mapper;

  @Override
  public CivilStatusResponse save(final CivilStatusRequest request) {
    return mapper.modelToResponse(service.save(mapper.requestToModel(request)));
  }

  @Override
  public List<CivilStatusResponse> findAll() {
    return mapper.modelsToResponseList(service.findAll());
  }

  @Override
  public CivilStatusResponse findById(final Integer id) {
    return mapper.modelToResponse(service.findById(id));
  }

  @Override
  public void deleteById(final Integer id) {
    service.deleteById(id);
  }
}
