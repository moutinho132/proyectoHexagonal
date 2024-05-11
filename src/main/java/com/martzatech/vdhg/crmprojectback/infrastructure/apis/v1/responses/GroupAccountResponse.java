package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
@Builder
@JsonInclude(Include.NON_EMPTY)
public class GroupAccountResponse implements Serializable {

  @Serial
  private static final long serialVersionUID = 702123829866067502L;

  private final Integer id;

  private final String name;

  private final String email;

  private final String industry;

  private final String vat;

  private final String alias;

  private final CustomerResponse owner;
  private final ChatRoomResponse chatRoom;

  private final List<AssociatedResponse> associates;

  private final UserResponse creationUser;

  private final UserResponse modificationUser;

  private final LocalDateTime creationTime;

  private final LocalDateTime modificationTime;
}
