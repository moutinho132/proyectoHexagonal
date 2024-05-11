package com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions;

import java.io.Serial;

public class VendorContactNotFoundException extends NotFoundException {

  @Serial
  private static final long serialVersionUID = -4859745897388504910L;

  private static final String RESOURCE = "Vendor Contact";

  public VendorContactNotFoundException(final Integer id) {
    super(id, RESOURCE);
  }
}
