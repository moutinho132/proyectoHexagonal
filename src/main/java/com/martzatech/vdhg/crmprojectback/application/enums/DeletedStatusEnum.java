package com.martzatech.vdhg.crmprojectback.application.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DeletedStatusEnum {
  ACTIVE(1),
  DELETED(2);

  final Integer id;
}
