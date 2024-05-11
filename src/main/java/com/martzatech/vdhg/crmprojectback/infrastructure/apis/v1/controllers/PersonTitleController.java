package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.controllers;

import com.martzatech.vdhg.crmprojectback.domains.commons.services.PersonTitleService;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.PersonTitleApi;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.PersonTitleApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.PersonTitleRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.PersonTitleResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/person-titles")
@Validated
public class PersonTitleController implements PersonTitleApi {

  private PersonTitleService service;

  private PersonTitleApiMapper mapper;

  @Override
  public PersonTitleResponse save(final PersonTitleRequest request) {
    return mapper.modelToResponse(service.save(mapper.requestToModel(request)));
  }

  @Override
  public List<PersonTitleResponse> findAll() {
    return mapper.modelsToResponseList(service.findAll());
  }

  @Override
  public PersonTitleResponse findById(final Integer id) {
    return mapper.modelToResponse(service.findById(id));
  }

  @Override
  public void deleteById(final Integer id) {
    service.deleteById(id);
  }
}
