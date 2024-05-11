package com.martzatech.vdhg.crmprojectback.domains.customers.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class PaymentMethod {

  private final Integer id;
  private final String name;
}
