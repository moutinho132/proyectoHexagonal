package com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions;

import java.io.Serial;

public class CategoryNotFoundException extends NotFoundException {

  @Serial
  private static final long serialVersionUID = 4947153549239018596L;
  
  private static final String RESOURCE = "category";

  public CategoryNotFoundException(final Integer id) {
    super(id, RESOURCE);
  }
}
