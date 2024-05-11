package com.martzatech.vdhg.crmprojectback.domains.chat.models;

import com.martzatech.vdhg.crmprojectback.domains.chat.enums.ChatMessageTypeEnum;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.FileResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class ChatMessage {
  @With
  private final Integer id;

  @With
  private final ChatRoom chatRoom;

  @With
  private final ChatMessageTypeEnum type;

  @With
  private final List<FileResponse> files;

  @With
  private final User sender;

  @With
  private final LocalDateTime creationTime;

  @With
  private final boolean read;

  @With
  private final LocalDateTime readingTime;

  @With
  private final String value;

  private final List<ChatMessageReader> readers;
}
