package com.martzatech.vdhg.crmprojectback.application.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserTypeEnum {
  CRM(1),
  CUSTOMER(3),
  ASSOCIATE(4),
  MOBIL(5),
  SYSTEM(2);
  final Integer id;
}
