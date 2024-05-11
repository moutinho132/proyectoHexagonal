package com.martzatech.vdhg.crmprojectback.domains.chat.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ChatMessageTypeEnum {

  PLAIN_TEXT(1),
  FILE(2),
  SYSTEM(3);
  private final Integer id;

  public static ChatMessageTypeEnum getById(final Integer statusId) {
    return Arrays.stream(ChatMessageTypeEnum.values())
        .filter(e -> e.getId().intValue() == statusId)
        .findAny()
        .orElse(null);
  }
}
