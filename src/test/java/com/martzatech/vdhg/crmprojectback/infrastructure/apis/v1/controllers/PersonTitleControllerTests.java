package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.controllers;

import com.martzatech.vdhg.crmprojectback.domains.commons.models.PersonTitle;
import com.martzatech.vdhg.crmprojectback.domains.commons.services.PersonTitleService;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.PersonTitleApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.PersonTitleRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.PersonTitleResponse;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PersonTitleControllerTests {

  private static final Integer ANY_ID = 1;

  @InjectMocks
  private PersonTitleController tested;

  @Mock
  private PersonTitleService service;

  @Mock
  private PersonTitleApiMapper mapper;

  @Mock
  private PersonTitleRequest request;

  @Mock
  private PersonTitle model;

  @Mock
  private PersonTitleResponse response;

  @Test
  void whenSave_thenCallService() {
    Mockito.when(mapper.requestToModel(request)).thenReturn(model);
    Mockito.when(service.save(model)).thenReturn(model);
    Mockito.when(mapper.modelToResponse(model)).thenReturn(response);

    final PersonTitleResponse actual = tested.save(request);

    Assertions.assertEquals(response, actual);
  }

  @Test
  void whenFindAll_thenCallService() {
    Mockito.when(service.findAll()).thenReturn(List.of(model));
    Mockito.when(mapper.modelsToResponseList(List.of(model))).thenReturn(List.of(response));

    final List<PersonTitleResponse> actual = tested.findAll();

    Assertions.assertEquals(List.of(response), actual);
  }

  @Test
  void whenFindById_thenCallService() {
    Mockito.when(service.findById(ANY_ID)).thenReturn(model);
    Mockito.when(mapper.modelToResponse(model)).thenReturn(response);

    final PersonTitleResponse actual = tested.findById(ANY_ID);

    Assertions.assertEquals(response, actual);
  }

  @Test
  void whenDeleteById_thenCallService() {
    Assertions.assertDoesNotThrow(() -> tested.deleteById(ANY_ID));

    Mockito.verify(service).deleteById(ANY_ID);
  }
}
