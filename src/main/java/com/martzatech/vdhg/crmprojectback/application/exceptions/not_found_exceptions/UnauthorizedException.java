package com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = 5535180973179563612L;

  public UnauthorizedException(final String msg) {
    super( msg);
  }
}
