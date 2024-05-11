package com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions;

import java.io.Serial;

public class ProductNotFoundException extends NotFoundException {

  @Serial
  private static final long serialVersionUID = -776160535711423552L;

  private static final String RESOURCE = "product";

  public ProductNotFoundException(final Integer id) {
    super(id, RESOURCE);
  }
}
