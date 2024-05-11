package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.controllers;

import com.martzatech.vdhg.crmprojectback.domains.customers.services.CompanyService;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.CompanyApi;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.CompanyApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.CompanyRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.CompanyResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/companies")
public class CompanyController implements CompanyApi {

  private CompanyService service;

  private CompanyApiMapper mapper;

  @Override
  public CompanyResponse save(final CompanyRequest request) {
    return mapper.modelToResponse(service.save(mapper.requestToModel(request)));
  }

  @Override
  public List<CompanyResponse> findAll() {
    return mapper.modelsToResponseList(service.findAll());
  }

  @Override
  public CompanyResponse findById(final Integer id) {
    return mapper.modelToResponse(service.findById(id));
  }

  @Override
  public void deleteById(final Integer id) {
    service.deleteById(id);
  }
}
