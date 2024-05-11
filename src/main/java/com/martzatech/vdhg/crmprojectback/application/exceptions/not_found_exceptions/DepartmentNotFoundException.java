package com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions;

import java.io.Serial;

public class DepartmentNotFoundException extends NotFoundException {

  @Serial
  private static final long serialVersionUID = -3376774170767612512L;

  private static final String RESOURCE = "department";

  public DepartmentNotFoundException(final Integer id) {
    super(id, RESOURCE);
  }
}
