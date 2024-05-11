package com.martzatech.vdhg.crmprojectback.domains.customers.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;

@AllArgsConstructor
@Getter
@Builder
public class SubCategory {

  private final Integer id;

  private final String name;

  @With
  private final Category category;
}
