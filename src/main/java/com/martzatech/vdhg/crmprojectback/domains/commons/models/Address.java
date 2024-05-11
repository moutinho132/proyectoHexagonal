package com.martzatech.vdhg.crmprojectback.domains.commons.models;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;

@AllArgsConstructor
@Getter
@Builder
public class Address {

  private final Integer id;

  @With
  private final Country country;

  @With
  private final String province;

  @With
  private final String street;

  @With
  private final String complement;

  @With
  private final String zipCode;

  @With
  private final String city;

  @With
  private final AttributeType attributeType;

  @With
  private LocalDateTime creationTime;

  @With
  private LocalDateTime modificationTime;
}
