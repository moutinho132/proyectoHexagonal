package com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions;

import java.io.Serial;

public class MembershipTypeNotFoundException extends NotFoundException {

  @Serial
  private static final long serialVersionUID = -4288321783048989189L;

  private static final String RESOURCE = "membership-type";

  public MembershipTypeNotFoundException(final Integer id) {
    super(id, RESOURCE);
  }
}
