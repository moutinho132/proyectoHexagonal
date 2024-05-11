package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.martzatech.vdhg.crmprojectback.domains.security.models.DeletedStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
@JsonInclude(Include.NON_EMPTY)
public class RoleResponse implements Serializable {

  @Serial
  private static final long serialVersionUID = 418143891276641514L;

  private final Integer id;
  private final String name;
  private final String description;
  private final DepartmentResponse department;
  private final List<PermissionResponse> permissions;
  private final LocalDateTime creationTime;
  private final LocalDateTime modificationTime;
  private final UserResponse creationUser;
  private final UserResponse modificationUser;
  private final List<UserResponse> users;
}
