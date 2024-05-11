package com.martzatech.vdhg.crmprojectback.domains.customers.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Builder
public class ProductLocation{

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
