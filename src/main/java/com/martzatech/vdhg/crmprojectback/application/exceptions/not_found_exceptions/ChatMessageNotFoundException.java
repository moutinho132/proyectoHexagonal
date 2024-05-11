package com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions;

import java.io.Serial;

public class ChatMessageNotFoundException extends NotFoundException {

  @Serial
  private static final long serialVersionUID = -3333316052493243360L;

  private static final String RESOURCE = "chat-message";

  public ChatMessageNotFoundException(final Integer id) {
    super(id, RESOURCE);
  }
}
