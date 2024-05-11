package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.controllers;

import com.martzatech.vdhg.crmprojectback.domains.commons.models.Language;
import com.martzatech.vdhg.crmprojectback.domains.commons.services.LanguageService;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.LanguageApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.LanguageResponse;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LanguageControllerTests {

  private static final Integer ANY_ID = 1;

  @InjectMocks
  private LanguageController tested;

  @Mock
  private LanguageService service;

  @Mock
  private LanguageApiMapper mapper;

  @Mock
  private Language model;

  @Mock
  private LanguageResponse response;

  @Test
  void whenFindAll_thenCallService() {
    Mockito.when(service.findAll()).thenReturn(List.of(model));
    Mockito.when(mapper.modelsToResponseList(List.of(model))).thenReturn(List.of(response));

    final List<LanguageResponse> actual = tested.findAll();

    Assertions.assertEquals(List.of(response), actual);
  }

  @Test
  void whenFindById_thenCallService() {
    Mockito.when(service.findById(ANY_ID)).thenReturn(model);
    Mockito.when(mapper.modelToResponse(model)).thenReturn(response);

    final LanguageResponse actual = tested.findById(ANY_ID);

    Assertions.assertEquals(response, actual);
  }
}
