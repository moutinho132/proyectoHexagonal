package com.martzatech.vdhg.crmprojectback.domains.customers.enums;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatusEnum {

  NEW(1),
  PAID(2),
  CLOSED(3),
  REOPEN(4);

  private final Integer id;

  public static OrderStatusEnum getById(final Integer statusId) {
    return Arrays.stream(OrderStatusEnum.values())
        .filter(e -> e.getId().intValue() == statusId)
        .findAny()
        .orElse(null);
  }
}
