package com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions;

import java.io.Serial;

public class NoteNotFoundException extends NotFoundException {

  @Serial
  private static final long serialVersionUID = 6786697232334645432L;

  private static final String RESOURCE = "note";

  public NoteNotFoundException(final Integer id) {
    super(id, RESOURCE);
  }
}
