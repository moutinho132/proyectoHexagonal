package com.martzatech.vdhg.crmprojectback.domains.customers.models;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class Discount {

  private Integer id;
  private String type;
  private BigDecimal value;
}
