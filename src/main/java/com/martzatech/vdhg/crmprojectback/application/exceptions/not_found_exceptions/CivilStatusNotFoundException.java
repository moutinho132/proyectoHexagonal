package com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions;

import java.io.Serial;

public class CivilStatusNotFoundException extends NotFoundException {

  @Serial
  private static final long serialVersionUID = 6334073673084685934L;

  private static final String RESOURCE = "civil-status";

  public CivilStatusNotFoundException(final Integer id) {
    super(id, RESOURCE);
  }
}
