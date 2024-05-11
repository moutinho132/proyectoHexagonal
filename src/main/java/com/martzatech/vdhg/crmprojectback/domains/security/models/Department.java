package com.martzatech.vdhg.crmprojectback.domains.security.models;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;

@AllArgsConstructor
@Getter
@Builder
public class Department {

  private final Integer id;

  @With
  private final String name;

  @With
  private final String description;

  @With
  private final Subsidiary subsidiary;

  private final List<Role> roles;

  @With
  private final LocalDateTime creationTime;

  @With
  private final LocalDateTime modificationTime;

  @With
  private final DeletedStatus status;

  @With
  private final User creationUser;

  @With
  private final User modificationUser;

  @With
  private final List<User> users;
}
