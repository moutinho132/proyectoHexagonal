package com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions;

import java.io.Serial;

public class EmailNotFoundException extends NotFoundException {

  @Serial
  private static final long serialVersionUID = 4102355001970172002L;

  private static final String RESOURCE = "email";

  public EmailNotFoundException(final Integer id) {
    super(id, RESOURCE);
  }
}
