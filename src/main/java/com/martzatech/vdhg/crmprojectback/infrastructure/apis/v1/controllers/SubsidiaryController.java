package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.controllers;

import com.martzatech.vdhg.crmprojectback.domains.security.services.SubsidiaryService;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.SubsidiaryApi;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.SubsidiaryApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.SubsidiaryRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.SubsidiaryResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/subsidiaries")
@Validated
public class SubsidiaryController implements SubsidiaryApi {

  private SubsidiaryService service;

  private SubsidiaryApiMapper mapper;

  @Override
  public SubsidiaryResponse save(final SubsidiaryRequest request) {
    return mapper.modelToResponse(service.save(mapper.requestToModel(request)));
  }

  @Override
  public List<SubsidiaryResponse> findAll() {
    return mapper.modelsToResponseList(service.findAll());
  }

  @Override
  public SubsidiaryResponse findById(final Integer id) {
    return mapper.modelToResponse(service.findById(id));
  }

  @Override
  public void deleteById(final Integer id) {
    service.deleteById(id);
  }
}
