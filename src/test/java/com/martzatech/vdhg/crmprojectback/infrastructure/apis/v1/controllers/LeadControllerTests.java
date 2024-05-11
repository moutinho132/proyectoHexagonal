package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.controllers;

import com.martzatech.vdhg.crmprojectback.application.services.LeadManagementService;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Lead;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.LeadService;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.LeadSpecification;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.LeadApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.LeadRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.LeadResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.PaginatedResponse;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LeadControllerTests {

  private static final Integer ANY_ID = 1;

  private static final Long ANY_LONG = 1L;

  private static final String ANY_STRING = "any";

  @InjectMocks
  private LeadController tested;

  @Mock
  private LeadManagementService managementService;

  @Mock
  private LeadService service;

  @Mock
  private LeadApiMapper mapper;

  @Mock
  private LeadRequest request;

  @Mock
  private Lead model;

  @Mock
  private LeadResponse response;

  @Mock
  private LeadSpecification specification;

  @Test
  void whenSave_thenCallService() {
    Mockito.when(mapper.requestToModel(request)).thenReturn(model);
    Mockito.when(managementService.save(model)).thenReturn(model);
    Mockito.when(mapper.modelToResponse(model)).thenReturn(response);

    final LeadResponse actual = tested.save(request);

    Assertions.assertEquals(response, actual);
  }

  @Test
  void whenFindAll_thenCallService() {
    Mockito.when(service.count(specification)).thenReturn(ANY_LONG);
    Mockito.when(managementService.findAll(specification, ANY_ID, ANY_ID, ANY_STRING, ANY_STRING))
        .thenReturn(List.of(model));
    Mockito.when(mapper.modelsToResponseList(List.of(model))).thenReturn(List.of(response));

    final PaginatedResponse<LeadResponse> actual = tested.findAll(specification, ANY_ID, ANY_ID, ANY_STRING,
        ANY_STRING);

    Assertions.assertNotNull(actual);
    Assertions.assertEquals(List.of(response), actual.getItems());
    Assertions.assertEquals(ANY_LONG.intValue(), actual.getTotal());
  }

  @Test
  void whenFindById_thenCallService() {
    Mockito.when(service.findById(ANY_ID)).thenReturn(model);
    Mockito.when(mapper.modelToResponse(model)).thenReturn(response);

    final LeadResponse actual = tested.findById(ANY_ID);

    Assertions.assertEquals(response, actual);
  }

  @Test
  void whenDeleteById_thenCallService() {
    Assertions.assertDoesNotThrow(() -> tested.deleteById(ANY_ID));

    Mockito.verify(managementService).deleteById(ANY_ID);
  }
}
