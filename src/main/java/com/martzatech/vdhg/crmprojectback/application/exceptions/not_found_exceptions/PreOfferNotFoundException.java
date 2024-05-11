package com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions;

import java.io.Serial;

public class PreOfferNotFoundException extends NotFoundException {

  @Serial
  private static final long serialVersionUID = 568390131389921635L;

  private static final String RESOURCE = "pre-offer";

  public PreOfferNotFoundException(final Integer id) {
    super(id, RESOURCE);
  }
}
