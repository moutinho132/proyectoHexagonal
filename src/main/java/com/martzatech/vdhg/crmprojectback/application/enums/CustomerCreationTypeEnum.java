package com.martzatech.vdhg.crmprojectback.application.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CustomerCreationTypeEnum {
  MANUAL(1),
  CONVERTED(2);

  final Integer id;
}
