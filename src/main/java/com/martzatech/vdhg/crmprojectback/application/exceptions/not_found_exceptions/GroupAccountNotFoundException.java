package com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions;

import java.io.Serial;

public class GroupAccountNotFoundException extends NotFoundException {

  @Serial
  private static final long serialVersionUID = 6562206685742019139L;

  private static final String RESOURCE = "Group account not found please verify and try again";

  public GroupAccountNotFoundException(final Integer id) {
    super(id, RESOURCE);
  }
}
