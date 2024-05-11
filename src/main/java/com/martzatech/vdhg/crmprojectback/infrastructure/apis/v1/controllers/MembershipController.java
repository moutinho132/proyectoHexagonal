package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.controllers;

import com.martzatech.vdhg.crmprojectback.application.services.MembershipManagementService;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.MembershipService;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.MembershipApi;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.MembershipApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.MembershipRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.MembershipResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/memberships")
@Validated
public class MembershipController implements MembershipApi {

  private MembershipManagementService managementService;
  private MembershipService service;
  private MembershipApiMapper mapper;

  @Override
  public MembershipResponse save(final MembershipRequest request) {
    return mapper.modelToResponse(managementService.save(mapper.requestToModel(request)));
  }

  @Override
  public List<MembershipResponse> findAll() {
    return mapper.modelsToResponseList(service.findAll());
  }

  @Override
  public MembershipResponse findById(final Integer id) {
    return mapper.modelToResponse(service.findById(id));
  }

  @Override
  public void deleteById(final Integer id) {
    service.deleteById(id);
  }
}
