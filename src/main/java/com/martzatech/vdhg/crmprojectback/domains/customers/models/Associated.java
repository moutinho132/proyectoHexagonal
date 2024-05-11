package com.martzatech.vdhg.crmprojectback.domains.customers.models;

import com.martzatech.vdhg.crmprojectback.domains.commons.models.Language;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;

@AllArgsConstructor
@Getter
@Builder
public class Associated {

  private final Integer id;

  @With
  private final String name;

  @With
  private final String surname;

  @With
  private final String email;

  @With
  private final String position;

  @With
  private final Boolean mainContact;

  @With
  private final GroupAccount groupAccount;

  @With
  private final String phonePrefix;

  @With
  private final String phoneNumber;

  @With
  private final Language preferredLanguage;

  @With
  private final User creationUser;

  @With
  private final User modificationUser;

  @With
  private final LocalDateTime creationTime;

  @With
  private final LocalDateTime modificationTime;
}
