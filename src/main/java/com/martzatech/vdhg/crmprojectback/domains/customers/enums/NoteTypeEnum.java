package com.martzatech.vdhg.crmprojectback.domains.customers.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum NoteTypeEnum {

  LEAD(1),
  CUSTOMER(2),
  PRODUCT(3),
  ORDER(4),
  VENDOR(5);

  private final Integer id;

  public static NoteTypeEnum getById(final Integer statusId) {
    return Arrays.stream(NoteTypeEnum.values())
        .filter(e -> e.getId().intValue() == statusId)
        .findAny()
        .orElse(null);
  }
}
