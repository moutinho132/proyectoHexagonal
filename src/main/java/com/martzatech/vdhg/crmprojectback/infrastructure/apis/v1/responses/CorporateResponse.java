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
public class CorporateResponse implements Serializable {

  @Serial
  private static final long serialVersionUID = 5221918723746852493L;

  private final Integer id;

  private final CustomerResponse mainContact;

  private final List<CustomerResponse> customers;

  private final DeletedStatus status;

  private final String name;

  private final String alias;

  private final String industry;

  private final String vat;

  private final String email;

  private final UserResponse creationUser;

  private final UserResponse modificationUser;

  private final LocalDateTime creationTime;

  private final LocalDateTime modificationTime;
}
