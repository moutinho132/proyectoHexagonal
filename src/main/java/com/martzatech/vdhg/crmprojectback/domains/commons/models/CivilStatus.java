package com.martzatech.vdhg.crmprojectback.domains.commons.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class CivilStatus {

  private final Integer id;
  private final String name;
}
