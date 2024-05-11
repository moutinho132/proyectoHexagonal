package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.controllers;

import com.martzatech.vdhg.crmprojectback.domains.customers.services.PaymentMethodService;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.PaymentMethodApi;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.PaymentMethodApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.PaymentMethodResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/payment-methods")
public class PaymentMethodController implements PaymentMethodApi {

  private PaymentMethodService service;

  private PaymentMethodApiMapper mapper;

  @Override
  public List<PaymentMethodResponse> findAll() {
    return mapper.modelsToResponseList(service.findAll());
  }

}
