package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Builder
@JsonInclude(Include.NON_EMPTY)
public class ProductOfferPriceBokingsMobileResponse implements Serializable {

  @Serial
  private static final long serialVersionUID = 2811519614268981995L;
  private final String productName;
  private final Integer productId;
  private final Boolean requiresPayment;
  private final Boolean showPrice;
  private final BigDecimal vatPercentage;
  private final String description;
  private final String paymentReference;
  private final String paymentMethod;
  private final BigDecimal totalWithCommission;
  private final BigDecimal totalWithVat;
}
