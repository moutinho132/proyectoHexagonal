package com.martzatech.vdhg.crmprojectback.domains.customers.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum PreOfferStatusEnum {

  WORKING(1),
  CLOSED(2);

  private final Integer id;

  public static PreOfferStatusEnum getById(final Integer statusId) {
    return Arrays.stream(PreOfferStatusEnum.values())
        .filter(e -> e.getId().intValue() == statusId)
        .findAny()
        .orElse(null);
  }
}
