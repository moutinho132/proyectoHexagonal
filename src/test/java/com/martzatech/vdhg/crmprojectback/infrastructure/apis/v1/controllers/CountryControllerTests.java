package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.controllers;

import com.martzatech.vdhg.crmprojectback.domains.commons.models.Country;
import com.martzatech.vdhg.crmprojectback.domains.commons.services.CountryService;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.CountryApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.CountryRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.CountryResponse;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CountryControllerTests {

  private static final Integer ANY_ID = 1;

  @InjectMocks
  private CountryController tested;

  @Mock
  private CountryService service;

  @Mock
  private CountryApiMapper mapper;

  @Mock
  private CountryRequest request;

  @Mock
  private Country model;

  @Mock
  private CountryResponse response;

  @Test
  void whenFindAll_thenCallService() {
    Mockito.when(service.findAll()).thenReturn(List.of(model));
    Mockito.when(mapper.modelsToResponseList(List.of(model))).thenReturn(List.of(response));

    final List<CountryResponse> actual = tested.findAll();

    Assertions.assertEquals(List.of(response), actual);
  }

  @Test
  void whenFindById_thenCallService() {
    Mockito.when(service.findById(ANY_ID)).thenReturn(model);
    Mockito.when(mapper.modelToResponse(model)).thenReturn(response);

    final CountryResponse actual = tested.findById(ANY_ID);

    Assertions.assertEquals(response, actual);
  }
}
