package com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions;

import java.io.Serial;

public class ChatRoomNotFoundException extends NotFoundException {

  private static final String RESOURCE = "chat-room";

  @Serial
  private static final long serialVersionUID = 8547097207180001862L;

  public ChatRoomNotFoundException(final Integer id) {
    super(id, RESOURCE);
  }
}
