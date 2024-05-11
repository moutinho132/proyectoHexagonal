package com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions;

import java.io.Serial;

public class CorporateNotFoundException extends NotFoundException {

  @Serial
  private static final long serialVersionUID = 6494037416165795809L;

  private static final String RESOURCE = "corporate";

  public CorporateNotFoundException(final Integer id) {
    super(id, RESOURCE);
  }
}
