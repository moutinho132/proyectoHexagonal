package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.controllers;

import com.martzatech.vdhg.crmprojectback.domains.security.models.Subsidiary;
import com.martzatech.vdhg.crmprojectback.domains.security.services.SubsidiaryService;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.SubsidiaryApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.SubsidiaryRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.SubsidiaryResponse;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SubsidiaryControllerTests {

  private static final Integer ANY_ID = 1;

  @InjectMocks
  private SubsidiaryController tested;

  @Mock
  private SubsidiaryService service;

  @Mock
  private SubsidiaryApiMapper mapper;

  @Mock
  private SubsidiaryRequest request;

  @Mock
  private Subsidiary model;

  @Mock
  private SubsidiaryResponse response;

  @Test
  void whenSave_thenCallService() {
    Mockito.when(mapper.requestToModel(request)).thenReturn(model);
    Mockito.when(service.save(model)).thenReturn(model);
    Mockito.when(mapper.modelToResponse(model)).thenReturn(response);

    final SubsidiaryResponse actual = tested.save(request);

    Assertions.assertEquals(response, actual);
  }

  @Test
  void whenFindAll_thenCallService() {
    Mockito.when(service.findAll()).thenReturn(List.of(model));
    Mockito.when(mapper.modelsToResponseList(List.of(model))).thenReturn(List.of(response));

    final List<SubsidiaryResponse> actual = tested.findAll();

    Assertions.assertEquals(List.of(response), actual);
  }

  @Test
  void whenFindById_thenCallService() {
    Mockito.when(service.findById(ANY_ID)).thenReturn(model);
    Mockito.when(mapper.modelToResponse(model)).thenReturn(response);

    final SubsidiaryResponse actual = tested.findById(ANY_ID);

    Assertions.assertEquals(response, actual);
  }

  @Test
  void whenDeleteById_thenCallService() {
    Assertions.assertDoesNotThrow(() -> tested.deleteById(ANY_ID));

    Mockito.verify(service).deleteById(ANY_ID);
  }
}
