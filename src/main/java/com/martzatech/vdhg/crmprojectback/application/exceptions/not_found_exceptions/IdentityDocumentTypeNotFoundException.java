package com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions;

import java.io.Serial;

public class IdentityDocumentTypeNotFoundException extends NotFoundException {

  @Serial
  private static final long serialVersionUID = -4762350925403155520L;

  private static final String RESOURCE = "identity-document-type";

  public IdentityDocumentTypeNotFoundException(final Integer id) {
    super(id, RESOURCE);
  }
}
