package com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions;

import java.io.Serial;

public class AttributeTypeNotFoundException extends NotFoundException {

  @Serial
  private static final long serialVersionUID = -5453717577637632573L;

  private static final String RESOURCE = "attributeType";

  public AttributeTypeNotFoundException(final Integer id) {
    super(id, RESOURCE);
  }
}
