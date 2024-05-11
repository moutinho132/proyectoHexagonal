package com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions;

import java.io.Serial;

public class SubsidiaryNotFoundException extends NotFoundException {

  @Serial
  private static final long serialVersionUID = -5762833577139546412L;

  private static final String RESOURCE = "subsidiary";

  public SubsidiaryNotFoundException(final Integer id) {
    super(id, RESOURCE);
  }
}
