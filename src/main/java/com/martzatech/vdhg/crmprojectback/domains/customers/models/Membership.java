package com.martzatech.vdhg.crmprojectback.domains.customers.models;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class Membership {

  private Integer id;
  private String name;
  private MembershipType type;
  private String description;
  private BigDecimal price;
  private String features;
  private Boolean priority;
}
