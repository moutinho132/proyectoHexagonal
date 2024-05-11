package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
@JsonInclude(Include.NON_EMPTY)
public class AssociatedResponse implements Serializable {

  @Serial
  private static final long serialVersionUID = 2519845496472120125L;

  private final Integer id;

  private final String name;

  private final String surname;

  private final String email;

  private final String position;

  private final Boolean mainContact;
  private final GroupAccountResponse groupAccount;

  private final CustomerResponse owner;

  private final String phonePrefix;

  private final String phoneNumber;

  private final LanguageResponse preferredLanguage;

  private final UserResponse creationUser;

  private final UserResponse modificationUser;

  private final LocalDateTime creationTime;

  private final LocalDateTime modificationTime;
}
