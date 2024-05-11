package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.controllers;

import com.martzatech.vdhg.crmprojectback.application.services.NoteManagementService;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Note;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.NoteService;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.NoteSpecification;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.NoteApi;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.NoteApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.NoteResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.PaginatedResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/notes")
@Validated
public class NoteController implements NoteApi {

  private NoteManagementService managementService;
  private NoteService service;
  private NoteApiMapper mapper;

  @Override
  public PaginatedResponse<NoteResponse> findAll(final NoteSpecification specification,
      final Integer page, final Integer size, final String direction, final String attribute) {
    final Long total = managementService.count(specification);
    final List<Note> response = managementService.findAll(specification, page, size, direction, attribute);
    return PaginatedResponse.<NoteResponse>builder()
        .total(total.intValue())
        .page(page)
        .size(response.size())
        .items(mapper.modelsToResponseList(response))
        .build();
  }

  @Override
  public NoteResponse findById(final Integer id) {
    return mapper.modelToResponse(service.findById(id));
  }

  public void deleteById(final Integer id) {
    service.deleteById(id);
  }

  public void deleteStatus(final Integer id) {
    service.deleteStatus(id);
  }
}
