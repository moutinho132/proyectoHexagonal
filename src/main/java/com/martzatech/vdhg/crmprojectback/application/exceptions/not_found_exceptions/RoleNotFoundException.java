package com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions;

import java.io.Serial;

public class RoleNotFoundException extends NotFoundException {

  @Serial
  private static final long serialVersionUID = 8905064738613206866L;

  private static final String RESOURCE = "role";

  public RoleNotFoundException(final Integer id) {
    super(id, RESOURCE);
  }
}
