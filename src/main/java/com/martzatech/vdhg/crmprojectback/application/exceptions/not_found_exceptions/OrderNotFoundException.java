package com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions;

import java.io.Serial;

public class OrderNotFoundException extends NotFoundException {

  @Serial
  private static final long serialVersionUID = -3918445559915621045L;

  private static final String RESOURCE = "order";

  public OrderNotFoundException(final Integer id) {
    super(id, RESOURCE);
  }
}
