package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.controllers;

import com.martzatech.vdhg.crmprojectback.domains.customers.services.RegistrationTypeService;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.RegistrationTypeApi;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.RegistrationTypeApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.RegistrationTypeRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.RegistrationTypeResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/registration-types")
@Validated
public class RegistrationTypeController implements RegistrationTypeApi {

  private RegistrationTypeService service;

  private RegistrationTypeApiMapper mapper;

  @Override
  public RegistrationTypeResponse save(final RegistrationTypeRequest request) {
    return mapper.modelToResponse(service.save(mapper.requestToModel(request)));
  }

  @Override
  public List<RegistrationTypeResponse> findAll() {
    return mapper.modelsToResponseList(service.findAll());
  }

  @Override
  public RegistrationTypeResponse findById(final Integer id) {
    return mapper.modelToResponse(service.findById(id));
  }

  @Override
  public void deleteById(final Integer id) {
    service.deleteById(id);
  }
}
