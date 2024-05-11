package com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions;

import java.io.Serial;

public class VendorNotFoundException extends NotFoundException {

  @Serial
  private static final long serialVersionUID = -4859745897388504910L;

  private static final String RESOURCE = "Vendor";

  public VendorNotFoundException(final Integer id) {
    super(id, RESOURCE);
  }
}
