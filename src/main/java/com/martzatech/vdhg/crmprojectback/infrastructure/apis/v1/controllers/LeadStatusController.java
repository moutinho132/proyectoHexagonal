package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.controllers;

import com.martzatech.vdhg.crmprojectback.domains.customers.services.LeadStatusService;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.LeadStatusApi;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.LeadStatusApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.LeadStatusRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.LeadStatusResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/lead-status")
@Validated
public class LeadStatusController implements LeadStatusApi {

  private LeadStatusService service;

  private LeadStatusApiMapper mapper;

  @Override
  public LeadStatusResponse save(final LeadStatusRequest request) {
    return mapper.modelToResponse(service.save(mapper.requestToModel(request)));
  }

  @Override
  public List<LeadStatusResponse> findAll() {
    return mapper.modelsToResponseList(service.findAll());
  }

  @Override
  public LeadStatusResponse findById(final Integer id) {
    return mapper.modelToResponse(service.findById(id));
  }

  @Override
  public void deleteById(final Integer id) {
    service.deleteById(id);
  }
}
