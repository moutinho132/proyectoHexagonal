package com.martzatech.vdhg.crmprojectback.application.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CustomerStatusEnum {
  ACTIVE(1),
  INACTIVE(2),
  FROZEN(3);

  final Integer id;
}
