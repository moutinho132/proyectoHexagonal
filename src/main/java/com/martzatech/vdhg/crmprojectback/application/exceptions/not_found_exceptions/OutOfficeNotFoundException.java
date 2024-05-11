package com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions;

import java.io.Serial;

public class OutOfficeNotFoundException extends NotFoundException {

  @Serial
  private static final long serialVersionUID = 5576053558914248842L;

  private static final String RESOURCE = "OutOffice";

  public OutOfficeNotFoundException(final Integer id) {
    super(id, RESOURCE);
  }
}
