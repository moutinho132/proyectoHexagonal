package com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions;

import java.io.Serial;

public class UserNotFoundException extends NotFoundException {

  @Serial
  private static final long serialVersionUID = -2940978459360883331L;

  private static final String RESOURCE = "user";

  public UserNotFoundException(final Integer id) {
    super(id, RESOURCE);
  }
}
