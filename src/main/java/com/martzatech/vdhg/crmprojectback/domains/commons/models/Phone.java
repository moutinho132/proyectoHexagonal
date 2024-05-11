package com.martzatech.vdhg.crmprojectback.domains.commons.models;

import java.time.LocalDateTime;

import com.martzatech.vdhg.crmprojectback.domains.security.models.DeletedStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;

@AllArgsConstructor
@Getter
@Builder
public class Phone {

  private final Integer id;

  @With
  private final String code;

  @With
  private final String value;

  @With
  private final Boolean valid;

  @With
  private final AttributeType attributeType;

  @With
  private final DeletedStatus deletedStatus;

  @With
  private final LocalDateTime checkTime;

  @With
  private final LocalDateTime creationTime;

  @With
  private final LocalDateTime modificationTime;
}
