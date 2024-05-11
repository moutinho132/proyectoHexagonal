package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Builder
public class ProductLocationResponse {

  private Integer id;
  @With
  private final String address;
  @With
  private final String mapUrl;
  @With
  private final BigDecimal latitude;
  @With
  private final BigDecimal longitude;
  @With
  private final String placeId;
}
