package com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions;

import java.io.Serial;

public class LeadCustomerFileNotFoundException extends NotFoundException {

  @Serial
  private static final long serialVersionUID = 5535180973179563612L;

  private static final String RESOURCE = "leadCustomer-file";

  public LeadCustomerFileNotFoundException(final Integer id) {
    super(id, RESOURCE);
  }
}
