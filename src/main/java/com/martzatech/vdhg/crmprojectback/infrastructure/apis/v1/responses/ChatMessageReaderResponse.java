package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
@JsonInclude(Include.NON_EMPTY)
public class ChatMessageReaderResponse implements Serializable {

  @Serial
  private static final long serialVersionUID = 6890944945754886552L;

  private final Integer id;
  private final UserResponse reader;
  private final LocalDateTime readingTime;
}
