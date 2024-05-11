package com.martzatech.vdhg.crmprojectback.infrastructure.api.v2.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.UserResponse;
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
public class CustomerMobileResponse implements Serializable {

  @Serial
  private static final long serialVersionUID = 5368827122315307846L;

  private final Integer id;
  private final PersonMobileResponse person;
  private final LocalDateTime creationTime;
  private final LocalDateTime modificationTime;
}
