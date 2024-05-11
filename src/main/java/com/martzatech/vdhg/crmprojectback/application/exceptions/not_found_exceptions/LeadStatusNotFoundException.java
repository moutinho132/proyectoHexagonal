package com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions;

import java.io.Serial;

public class LeadStatusNotFoundException extends NotFoundException {

  @Serial
  private static final long serialVersionUID = -296421699049339360L;

  private static final String RESOURCE = "lead-status";

  public LeadStatusNotFoundException(final Integer id) {
    super(id, RESOURCE);
  }
}
