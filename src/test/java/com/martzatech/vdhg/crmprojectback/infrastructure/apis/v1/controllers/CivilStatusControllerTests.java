package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.controllers;

import com.martzatech.vdhg.crmprojectback.domains.commons.models.CivilStatus;
import com.martzatech.vdhg.crmprojectback.domains.commons.services.CivilStatusService;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.CivilStatusApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.CivilStatusRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.CivilStatusResponse;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CivilStatusControllerTests {

  private static final Integer ANY_ID = 1;

  @InjectMocks
  private CivilStatusController tested;

  @Mock
  private CivilStatusService service;

  @Mock
  private CivilStatusApiMapper mapper;

  @Mock
  private CivilStatusRequest request;

  @Mock
  private CivilStatus model;

  @Mock
  private CivilStatusResponse response;

  @Test
  void whenSave_thenCallService() {
    Mockito.when(mapper.requestToModel(request)).thenReturn(model);
    Mockito.when(service.save(model)).thenReturn(model);
    Mockito.when(mapper.modelToResponse(model)).thenReturn(response);

    final CivilStatusResponse actual = tested.save(request);

    Assertions.assertEquals(response, actual);
  }

  @Test
  void whenFindAll_thenCallService() {
    Mockito.when(service.findAll()).thenReturn(List.of(model));
    Mockito.when(mapper.modelsToResponseList(List.of(model))).thenReturn(List.of(response));

    final List<CivilStatusResponse> actual = tested.findAll();

    Assertions.assertEquals(List.of(response), actual);
  }

  @Test
  void whenFindById_thenCallService() {
    Mockito.when(service.findById(ANY_ID)).thenReturn(model);
    Mockito.when(mapper.modelToResponse(model)).thenReturn(response);

    final CivilStatusResponse actual = tested.findById(ANY_ID);

    Assertions.assertEquals(response, actual);
  }

  @Test
  void whenDeleteById_thenCallService() {
    Assertions.assertDoesNotThrow(() -> tested.deleteById(ANY_ID));

    Mockito.verify(service).deleteById(ANY_ID);
  }
}
