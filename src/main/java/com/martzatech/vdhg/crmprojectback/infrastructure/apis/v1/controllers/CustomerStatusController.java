package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.controllers;

import com.martzatech.vdhg.crmprojectback.domains.customers.services.CustomerStatusService;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.CustomerStatusApi;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.CustomerStatusApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.CustomerStatusRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.CustomerStatusResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/customer-status")
@Validated
public class CustomerStatusController implements CustomerStatusApi {

  private CustomerStatusService service;

  private CustomerStatusApiMapper mapper;

  @Override
  public CustomerStatusResponse save(final CustomerStatusRequest request) {
    return mapper.modelToResponse(service.save(mapper.requestToModel(request)));
  }

  @Override
  public List<CustomerStatusResponse> findAll() {
    return mapper.modelsToResponseList(service.findAll());
  }

  @Override
  public CustomerStatusResponse findById(final Integer id) {
    return mapper.modelToResponse(service.findById(id));
  }

  @Override
  public void deleteById(final Integer id) {
    service.deleteById(id);
  }
}
