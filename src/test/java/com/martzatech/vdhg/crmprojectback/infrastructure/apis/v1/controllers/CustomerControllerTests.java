package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.controllers;

import com.martzatech.vdhg.crmprojectback.application.services.CustomerManagementService;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Customer;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.CustomerService;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.CustomerSpecification;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.CustomerApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.CustomerRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.CustomerResponse;
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
class CustomerControllerTests {

  private static final Integer ANY_ID = 1;

  private static final Long ANY_LONG = 1L;

  private static final String ANY_STRING = "any";

  @InjectMocks
  private CustomerController tested;

  @Mock
  private CustomerManagementService managementService;

  @Mock
  private CustomerService service;

  @Mock
  private CustomerApiMapper mapper;

  @Mock
  private CustomerRequest request;

  @Mock
  private Customer model;

  @Mock
  private CustomerResponse response;

  @Mock
  private CustomerSpecification specification;

  @Test
  void whenSave_thenCallService() {
    Mockito.when(mapper.requestToModel(request)).thenReturn(model);
    Mockito.when(managementService.saveCustomer(model)).thenReturn(model);
    Mockito.when(mapper.modelToResponse(model)).thenReturn(response);

    final CustomerResponse actual = tested.save(request);

    Assertions.assertEquals(response, actual);
  }

  @Test
  void whenFindAll_thenCallService() {
    Mockito.when(service.count(specification)).thenReturn(ANY_LONG);
    Mockito.when(managementService.findAll(specification, ANY_ID, ANY_ID, ANY_STRING, ANY_STRING))
        .thenReturn(List.of(model));
    Mockito.when(mapper.modelsToResponseList(List.of(model))).thenReturn(List.of(response));

    final PaginatedResponse<CustomerResponse> actual = tested.findAll(specification, ANY_ID, ANY_ID, ANY_STRING,
        ANY_STRING);

    Assertions.assertNotNull(actual);
    Assertions.assertEquals(List.of(response), actual.getItems());
    Assertions.assertEquals(ANY_LONG.intValue(), actual.getTotal());
  }

  @Test
  void whenFindById_thenCallService() {
    Mockito.when(service.findById(ANY_ID)).thenReturn(model);
    Mockito.when(mapper.modelToResponse(model)).thenReturn(response);

    final CustomerResponse actual = tested.findById(ANY_ID);

    Assertions.assertEquals(response, actual);
  }

  @Test
  void whenDeleteById_thenCallService() {
    Assertions.assertDoesNotThrow(() -> tested.deleteById(ANY_ID));

    Mockito.verify(managementService).deleteById(ANY_ID);
  }
}
