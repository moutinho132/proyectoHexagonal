package com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions;

import java.io.Serial;

public class PaymentMethodNotFoundException extends NotFoundException {

  @Serial
  private static final long serialVersionUID = -8206229869995498642L;

  private static final String RESOURCE = "payment-method";

  public PaymentMethodNotFoundException(final Integer id) {
    super(id, RESOURCE);
  }
}
