package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.controllers;

import com.martzatech.vdhg.crmprojectback.application.services.CorporateManagementService;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Corporate;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.CorporateService;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.CorporateSpecification;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.CorporateApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.CorporateRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.CorporateResponse;
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
class CorporateControllerTests {

  private static final Integer ANY_ID = 1;

  private static final Long ANY_LONG = 1L;

  private static final String ANY_STRING = "any";

  @InjectMocks
  private CorporateController tested;

  @Mock
  private CorporateManagementService managementService;

  @Mock
  private CorporateService service;

  @Mock
  private CorporateApiMapper mapper;

  @Mock
  private CorporateRequest request;

  @Mock
  private Corporate model;

  @Mock
  private CorporateResponse response;

  @Mock
  private CorporateSpecification specification;

  @Test
  void whenSave_thenCallService() {
    Mockito.when(mapper.requestToModel(request)).thenReturn(model);
    Mockito.when(managementService.save(model)).thenReturn(model);
    Mockito.when(mapper.modelToResponse(model)).thenReturn(response);

    final CorporateResponse actual = tested.save(request);

    Assertions.assertEquals(response, actual);
  }

  @Test
  void whenFindAll_thenCallService() {
    Mockito.when(service.count(specification)).thenReturn(ANY_LONG);
    Mockito.when(managementService.findAll(specification, ANY_ID, ANY_ID, ANY_STRING, ANY_STRING))
        .thenReturn(List.of(model));
    Mockito.when(mapper.modelsToResponseList(List.of(model))).thenReturn(List.of(response));

    final PaginatedResponse<CorporateResponse> actual = tested.findAll(specification, ANY_ID, ANY_ID, ANY_STRING,
        ANY_STRING);

    Assertions.assertNotNull(actual);
    Assertions.assertEquals(List.of(response), actual.getItems());
    Assertions.assertEquals(ANY_LONG.intValue(), actual.getTotal());
  }

  @Test
  void whenFindById_thenCallService() {
    Mockito.when(service.findById(ANY_ID)).thenReturn(model);
    Mockito.when(mapper.modelToResponse(model)).thenReturn(response);

    final CorporateResponse actual = tested.findById(ANY_ID);

    Assertions.assertEquals(response, actual);
  }

  @Test
  void whenDeleteById_thenCallService() {
    Assertions.assertDoesNotThrow(() -> tested.deleteById(ANY_ID));

    Mockito.verify(service).deleteById(ANY_ID);
  }
}
