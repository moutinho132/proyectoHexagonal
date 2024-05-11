package com.martzatech.vdhg.crmprojectback.domains.chat.enums;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChatRoomTypeEnum {

  INTERNAL(1),
  CUSTOMER(2),
  GROUPACCOUNT(3);
  private final Integer id;

  public static ChatRoomTypeEnum getById(final Integer statusId) {
    return Arrays.stream(ChatRoomTypeEnum.values())
        .filter(e -> e.getId().intValue() == statusId)
        .findAny()
        .orElse(null);
  }
}
