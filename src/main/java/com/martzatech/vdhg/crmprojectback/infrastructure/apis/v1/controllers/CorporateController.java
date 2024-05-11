package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.controllers;

import com.martzatech.vdhg.crmprojectback.application.services.CorporateManagementService;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Corporate;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.CorporateService;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.CorporateSpecification;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.CorporateApi;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.CorporateApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.CorporateRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.CorporateResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.PaginatedResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/corporates")
public class CorporateController implements CorporateApi {

  private CorporateService service;
  private CorporateManagementService managementService;
  private CorporateApiMapper mapper;

  @Override
  public CorporateResponse save(@Valid final CorporateRequest request) {
    return mapper.modelToResponse(managementService.save(mapper.requestToModel(request)));
  }

  @Override
  public PaginatedResponse<CorporateResponse> findAll(final CorporateSpecification specification,
      final Integer page, final Integer size, final String direction, final String attribute) {
    final Long total = service.count(specification);
    final List<Corporate> response = managementService.findAll(specification, page, size, direction, attribute);
    return PaginatedResponse.<CorporateResponse>builder()
        .total(total.intValue())
        .page(page)
        .size(response.size())
        .items(mapper.modelsToResponseList(response))
        .build();
  }

  @Override
  public CorporateResponse findById(final Integer id) {
    return mapper.modelToResponse(service.findById(id));
  }

  @Override
  public void deleteById(final Integer id) {
    service.deleteById(id);
  }

  @Override
  public void deleteStatus(final Integer id) {
    service.deleteStatus(id);
  }
}
