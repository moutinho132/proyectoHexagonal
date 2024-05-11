package com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions;

import java.io.Serial;

public class MembershipNotFoundException extends NotFoundException {

  @Serial
  private static final long serialVersionUID = 7721802487426107875L;

  private static final String RESOURCE = "membership";

  public MembershipNotFoundException(final Integer id) {
    super(id, RESOURCE);
  }
}
