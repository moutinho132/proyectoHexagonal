package com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions;

import java.io.Serial;

public class PersonTitleNotFoundException extends NotFoundException {

  @Serial
  private static final long serialVersionUID = -7350123081593290719L;

  private static final String RESOURCE = "person-title";

  public PersonTitleNotFoundException(final Integer id) {
    super(id, RESOURCE);
  }
}
