package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.martzatech.vdhg.crmprojectback.domains.chat.enums.ChatMessageTypeEnum;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
@JsonInclude(Include.NON_EMPTY)
public class ChatMessageResponse implements Serializable {

  @Serial
  private static final long serialVersionUID = -1307203667776452541L;

  private final Integer id;
  private final ChatMessageTypeEnum type;
  private final String value;
  private final Boolean read;
  private final LocalDateTime readingTime;
  private final List<ChatMessageReaderResponse> readers;
  private final UserResponse sender;
  private final List<FileResponse> files;
  private final ChatRoomResponse chatRoom;
  private final LocalDateTime creationTime;
}
