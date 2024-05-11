package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.controllers;

import com.martzatech.vdhg.crmprojectback.domains.commons.models.IdentityDocumentType;
import com.martzatech.vdhg.crmprojectback.domains.commons.services.IdentityDocumentTypeService;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.IdentityDocumentTypeApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.IdentityDocumentTypeRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.IdentityDocumentTypeResponse;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class IdentityDocumentTypeControllerTests {

  private static final Integer ANY_ID = 1;

  @InjectMocks
  private IdentityDocumentTypeController tested;

  @Mock
  private IdentityDocumentTypeService service;

  @Mock
  private IdentityDocumentTypeApiMapper mapper;

  @Mock
  private IdentityDocumentTypeRequest request;

  @Mock
  private IdentityDocumentType model;

  @Mock
  private IdentityDocumentTypeResponse response;

  @Test
  void whenSave_thenCallService() {
    Mockito.when(mapper.requestToModel(request)).thenReturn(model);
    Mockito.when(service.save(model)).thenReturn(model);
    Mockito.when(mapper.modelToResponse(model)).thenReturn(response);

    final IdentityDocumentTypeResponse actual = tested.save(request);

    Assertions.assertEquals(response, actual);
  }

  @Test
  void whenFindAll_thenCallService() {
    Mockito.when(service.findAll()).thenReturn(List.of(model));
    Mockito.when(mapper.modelsToResponseList(List.of(model))).thenReturn(List.of(response));

    final List<IdentityDocumentTypeResponse> actual = tested.findAll();

    Assertions.assertEquals(List.of(response), actual);
  }

  @Test
  void whenFindById_thenCallService() {
    Mockito.when(service.findById(ANY_ID)).thenReturn(model);
    Mockito.when(mapper.modelToResponse(model)).thenReturn(response);

    final IdentityDocumentTypeResponse actual = tested.findById(ANY_ID);

    Assertions.assertEquals(response, actual);
  }

  @Test
  void whenDeleteById_thenCallService() {
    Assertions.assertDoesNotThrow(() -> tested.deleteById(ANY_ID));

    Mockito.verify(service).deleteById(ANY_ID);
  }
}
