package com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions;

import java.io.Serial;

public class RegistrationTypeNotFoundException extends NotFoundException {

  @Serial
  private static final long serialVersionUID = 3356646664944102569L;

  private static final String RESOURCE = "registration-type";

  public RegistrationTypeNotFoundException(final Integer id) {
    super(id, RESOURCE);
  }
}
