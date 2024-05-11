package com.martzatech.vdhg.crmprojectback.domains.commons.models;

import lombok.*;

@AllArgsConstructor
@Getter
@Builder
public class DayOffice {
  private Integer id;

  @With
  private final String name;
}
