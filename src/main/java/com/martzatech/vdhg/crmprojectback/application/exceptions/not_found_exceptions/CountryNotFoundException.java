package com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions;

import java.io.Serial;

public class CountryNotFoundException extends NotFoundException {

  @Serial
  private static final long serialVersionUID = 107720373537274500L;

  private static final String RESOURCE = "country";

  public CountryNotFoundException(final Integer id) {
    super(id, RESOURCE);
  }
}
