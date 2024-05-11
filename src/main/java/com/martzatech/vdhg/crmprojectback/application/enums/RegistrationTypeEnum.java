package com.martzatech.vdhg.crmprojectback.application.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RegistrationTypeEnum {
  AUTOMATIC(1),
  MANUAL(2),
  REFERRED(3);

  final Integer id;
}
