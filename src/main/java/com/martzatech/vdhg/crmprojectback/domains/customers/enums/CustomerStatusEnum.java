package com.martzatech.vdhg.crmprojectback.domains.customers.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum CustomerStatusEnum {

  ACTIVE(1),
  FROZEN(2),
  INACTIVE(3);

  private final Integer id;
  public static CustomerStatusEnum getById(final Integer statusId) {
    return Arrays.stream(CustomerStatusEnum.values())
        .filter(e -> e.getId().intValue() == statusId)
        .findAny()
        .orElse(null);
  }
}
