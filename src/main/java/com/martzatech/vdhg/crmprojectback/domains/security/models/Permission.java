package com.martzatech.vdhg.crmprojectback.domains.security.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;

@AllArgsConstructor
@Getter
@Builder
public class Permission {

  @With
  private final Integer id;

  @With
  private final String name;

  private final String resource;

  private final String method;

  @With
  private final String field;

  @With
  private final Boolean checked;
}
