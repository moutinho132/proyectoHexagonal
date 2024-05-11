package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.controllers;

import com.martzatech.vdhg.crmprojectback.domains.commons.services.AttributeTypeService;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.AttributeTypeApi;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.AttributeTypeApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.AttributeTypeRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.AttributeTypeResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/attribute-types")
@Validated
public class AttributeTypeController implements AttributeTypeApi {

  private AttributeTypeService service;

  private AttributeTypeApiMapper mapper;

  @Override
  public AttributeTypeResponse save(final AttributeTypeRequest request) {
    return mapper.modelToResponse(service.save(mapper.requestToModel(request)));
  }

  @Override
  public List<AttributeTypeResponse> findAll() {
    return mapper.modelsToResponseList(service.findAll());
  }

  @Override
  public AttributeTypeResponse findById(final Integer id) {
    return mapper.modelToResponse(service.findById(id));
  }

  @Override
  public void deleteById(final Integer id) {
    service.deleteById(id);
  }
}
