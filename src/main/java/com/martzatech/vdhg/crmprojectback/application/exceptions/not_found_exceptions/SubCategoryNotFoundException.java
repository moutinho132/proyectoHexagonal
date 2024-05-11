package com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions;

import java.io.Serial;

public class SubCategoryNotFoundException extends NotFoundException {

  @Serial
  private static final long serialVersionUID = 5874798166247830845L;

  private static final String RESOURCE = "subCategory";

  public SubCategoryNotFoundException(final Integer id) {
    super(id, RESOURCE);
  }
}
