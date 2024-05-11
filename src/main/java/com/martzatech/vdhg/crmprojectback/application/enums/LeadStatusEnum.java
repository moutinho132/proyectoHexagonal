package com.martzatech.vdhg.crmprojectback.application.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum LeadStatusEnum {
  NEW(1),
  WORKING(2),
  NURTURING(3),
  CONVERTED(4),
  DISCARDED(5);

  final Integer id;
}
