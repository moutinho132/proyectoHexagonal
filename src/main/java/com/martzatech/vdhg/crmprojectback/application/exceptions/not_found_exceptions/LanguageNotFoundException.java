package com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions;

import java.io.Serial;

public class LanguageNotFoundException extends NotFoundException {

  @Serial
  private static final long serialVersionUID = 258167423146569850L;

  private static final String RESOURCE = "language";

  public LanguageNotFoundException(final Integer id) {
    super(id, RESOURCE);
  }
}
