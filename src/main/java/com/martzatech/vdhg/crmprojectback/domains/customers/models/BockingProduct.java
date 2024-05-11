package com.martzatech.vdhg.crmprojectback.domains.customers.models;

import com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants;
import com.martzatech.vdhg.crmprojectback.domains.vendors.models.Vendor;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.FileResponse;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class BockingProduct {
  @With
  private   Integer id;
  private final Integer productId;
  @With
  private final String productName;
  @With
  private final Vendor vendor;
  private final Boolean requiresPayment;
  private final Boolean showPrice;
  private final Boolean defaultBasePrice;
  private final BigDecimal basePrice;
  private final Boolean defaultVat;

  @DecimalMin(value = "00.00", message = CommonConstants.DECIMAL_MIN_MESSAGE_DEC)
  @Digits(integer = 9, fraction = 2, message = CommonConstants.EXPECTED_DIGITS_MESSAGE)
  @DecimalMax(value = "100.00", message = CommonConstants.DECIMAL_MAX_MESSAGE_DEC)
  private final BigDecimal vatPercentage;

   private final BigDecimal vatValue;

  private final Boolean defaultCommission;

  @DecimalMin(value = "00.00", message = CommonConstants.DECIMAL_MIN_MESSAGE_DEC)
  @Digits(integer = 9, fraction = 2, message = CommonConstants.EXPECTED_DIGITS_MESSAGE)
  @DecimalMax(value = "100.00", message = CommonConstants.DECIMAL_MAX_MESSAGE_DEC)
  private final BigDecimal commissionPercentage;

  private final BigDecimal commissionValue;

  @With
  private final BigDecimal totalWithCommission;//(base+(base*commssion)/100

  @With
  private final BigDecimal totalWithVat;// (totalWihtCommision+(totalWihtCommision * commssion)/100

  private final Boolean defaultDescription;

  private final String description;
  private final Boolean defaultMarketing;
  private final String marketing;
  private final Boolean defaultAvailabilityFrom;
  private final LocalDateTime availabilityFrom;
  private final Boolean defaultAvailabilityTo;
  private final LocalDateTime availabilityTo;
  private final String paymentReference;
  private final Boolean defaultPaymentMethod;
  private final String paymentMethod;
  private final Boolean defaultPaymentDetails;
  private final String paymentDetails;
  @With
  private  Product product;
  @With
  private  PreOffer preOffer;
  @With
  private  Offer offer;
  @With
  private  List<FileResponse> files;
}
