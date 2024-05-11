package com.martzatech.vdhg.crmprojectback.domains.customers.models;

import com.martzatech.vdhg.crmprojectback.domains.customers.enums.NoteTypeEnum;
import com.martzatech.vdhg.crmprojectback.domains.security.models.DeletedStatus;
import com.martzatech.vdhg.crmprojectback.domains.security.models.Role;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;

@AllArgsConstructor
@Getter
@Builder
public class Note {

  private final Integer id;

  @With
  private final String title;

  @With
  private final String description;

  @With
  private final NoteTypeEnum type;

  @With
  private final Integer elementId;

  @With
  private final NoteElement element;

  @With
  private final List<Role> roles;

  @With
  private final List<User> users;

  @With
  private final DeletedStatus status;

  @With
  private final User creationUser;

  @With
  private final User modificationUser;

  @With
  private final LocalDateTime creationTime;

  @With
  private final LocalDateTime modificationTime;
}
