package com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions;

import java.io.Serial;

public class AssociatedNotFoundException extends NotFoundException {

  @Serial
  private static final long serialVersionUID = 5576053558914248842L;

  private static final String RESOURCE = "associated";

  public AssociatedNotFoundException(final Integer id) {
    super(id, RESOURCE);
  }
}
