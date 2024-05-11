package com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions;

import java.io.Serial;

public class LeadNotFoundException extends NotFoundException {

  @Serial
  private static final long serialVersionUID = -7556764804595108046L;

  private static final String RESOURCE = "lead";

  public LeadNotFoundException(final Integer id) {
    super(id, RESOURCE);
  }
}
