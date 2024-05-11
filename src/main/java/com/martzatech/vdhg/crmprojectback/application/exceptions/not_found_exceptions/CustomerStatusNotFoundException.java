package com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions;

import java.io.Serial;

public class CustomerStatusNotFoundException extends NotFoundException {

  @Serial
  private static final long serialVersionUID = -8309909948257269351L;

  private static final String RESOURCE = "customer-status";

  public CustomerStatusNotFoundException(final Integer id) {
    super(id, RESOURCE);
  }
}
