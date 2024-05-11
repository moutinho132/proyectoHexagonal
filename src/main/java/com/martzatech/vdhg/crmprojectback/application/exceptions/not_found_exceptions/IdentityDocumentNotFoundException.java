package com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions;

import java.io.Serial;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class IdentityDocumentNotFoundException extends NotFoundException {

  @Serial
  private static final long serialVersionUID = 2298074427260153328L;

  private static final String RESOURCE = "identity-document";

  public IdentityDocumentNotFoundException(final Integer id) {
    super(id, RESOURCE);
  }
}
