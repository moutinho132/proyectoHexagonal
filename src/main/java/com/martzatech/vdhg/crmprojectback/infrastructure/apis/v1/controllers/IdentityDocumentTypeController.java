package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.controllers;

import com.martzatech.vdhg.crmprojectback.domains.commons.services.IdentityDocumentTypeService;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.IdentityDocumentTypeApi;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.IdentityDocumentTypeApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.IdentityDocumentTypeRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.IdentityDocumentTypeResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/identity-document-types")
@Validated
public class IdentityDocumentTypeController implements IdentityDocumentTypeApi {

  private IdentityDocumentTypeService service;

  private IdentityDocumentTypeApiMapper mapper;

  @Override
  public IdentityDocumentTypeResponse save(final IdentityDocumentTypeRequest request) {
    return mapper.modelToResponse(service.save(mapper.requestToModel(request)));
  }

  @Override
  public List<IdentityDocumentTypeResponse> findAll() {
    return mapper.modelsToResponseList(service.findAll());
  }

  @Override
  public IdentityDocumentTypeResponse findById(final Integer id) {
    return mapper.modelToResponse(service.findById(id));
  }

  @Override
  public void deleteById(final Integer id) {
    service.deleteById(id);
  }
}
