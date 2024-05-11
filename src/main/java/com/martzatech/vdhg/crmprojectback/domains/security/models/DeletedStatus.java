package com.martzatech.vdhg.crmprojectback.domains.security.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class DeletedStatus {

  private final Integer id;
  private final String name;
}
