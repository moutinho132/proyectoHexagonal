package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.martzatech.vdhg.crmprojectback.domains.security.models.DeletedStatus;
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
public class LeadCustomerFileResponse implements Serializable {

  @Serial
  private static final long serialVersionUID = 5588746633794586540L;

  private final Integer id;
  private final String url;
  private final String name;
  private final DeletedStatus status;
  private final UserResponse creationUser;
  private final LocalDateTime creationTime;
}
