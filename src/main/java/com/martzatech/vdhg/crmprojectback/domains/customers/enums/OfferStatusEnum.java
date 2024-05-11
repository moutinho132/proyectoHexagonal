package com.martzatech.vdhg.crmprojectback.domains.customers.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum OfferStatusEnum {

  OPEN(1),
  CONFIRMED(2),
  ACCEPTED(3),
  CLOSED(4),
  SENT(5);//SEND

  private final Integer id;

  public static OfferStatusEnum getById(final Integer statusId) {
    return Arrays.stream(OfferStatusEnum.values())
        .filter(e -> e.getId().intValue() == statusId)
        .findAny()
        .orElse(null);
  }
}
