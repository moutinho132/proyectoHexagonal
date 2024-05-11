package com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions;

import java.io.Serial;

public class PersonNotFoundException extends NotFoundException {

  @Serial
  private static final long serialVersionUID = 7676232994613029231L;

  private static final String RESOURCE = "person";

  public PersonNotFoundException(final Integer id) {
    super(id, RESOURCE);
  }
}
