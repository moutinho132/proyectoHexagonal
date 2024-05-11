package com.martzatech.vdhg.crmprojectback.domains.chat.models;

import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class ChatMessageReader {

  private final Integer id;
  private final ChatMessage message;
  private final User reader;
  private final LocalDateTime readingTime;
}
