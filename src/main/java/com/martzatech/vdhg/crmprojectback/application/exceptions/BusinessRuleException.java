package com.martzatech.vdhg.crmprojectback.application.exceptions;

import java.io.Serial;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BusinessRuleException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = -8433363895084938019L;

  private final String message;
}
