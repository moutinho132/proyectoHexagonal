package com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions;

import java.io.Serial;

public class CompanyNotFoundException extends NotFoundException {

  @Serial
  private static final long serialVersionUID = -3421940156796707636L;

  private static final String RESOURCE = "company";

  public CompanyNotFoundException(final Integer id) {
    super(id, RESOURCE);
  }
}
