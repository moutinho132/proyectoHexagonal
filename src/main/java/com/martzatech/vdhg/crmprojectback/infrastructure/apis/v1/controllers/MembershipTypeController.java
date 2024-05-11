package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.controllers;

import com.martzatech.vdhg.crmprojectback.domains.customers.services.MembershipTypeService;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.MembershipTypeApi;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.MembershipTypeApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.MembershipTypeRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.MembershipTypeResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/membership-types")
@Validated
public class MembershipTypeController implements MembershipTypeApi {

  private MembershipTypeService service;

  private MembershipTypeApiMapper mapper;

  @Override
  public MembershipTypeResponse save(final MembershipTypeRequest request) {
    return mapper.modelToResponse(service.save(mapper.requestToModel(request)));
  }

  @Override
  public List<MembershipTypeResponse> findAll() {
    return mapper.modelsToResponseList(service.findAll());
  }

  @Override
  public MembershipTypeResponse findById(final Integer id) {
    return mapper.modelToResponse(service.findById(id));
  }

  @Override
  public void deleteById(final Integer id) {
    service.deleteById(id);
  }
}
