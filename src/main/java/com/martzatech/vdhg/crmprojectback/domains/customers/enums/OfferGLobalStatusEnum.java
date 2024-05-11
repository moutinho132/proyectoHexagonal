package com.martzatech.vdhg.crmprojectback.domains.customers.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum OfferGLobalStatusEnum {

  WORKING(1),
  DECLINED(2),
  ACCEPTED(3);

  private final Integer id;

  public static OfferGLobalStatusEnum getById(final Integer statusId) {
    return Arrays.stream(OfferGLobalStatusEnum.values())
        .filter(e -> e.getId().intValue() == statusId)
        .findAny()
        .orElse(null);
  }
}
