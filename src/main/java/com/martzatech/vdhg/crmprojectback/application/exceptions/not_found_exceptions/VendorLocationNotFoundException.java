package com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions;

import java.io.Serial;

public class VendorLocationNotFoundException extends NotFoundException {

  @Serial
  private static final long serialVersionUID = -4859745897388504910L;

  private static final String RESOURCE = "Vendor Location";

  public VendorLocationNotFoundException(final Integer id) {
    super(id, RESOURCE);
  }
}
