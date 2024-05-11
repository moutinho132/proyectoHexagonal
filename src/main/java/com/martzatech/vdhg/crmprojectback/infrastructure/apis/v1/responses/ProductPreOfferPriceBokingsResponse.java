package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
@Builder
@JsonInclude(Include.NON_EMPTY)
public class ProductPreOfferPriceBokingsResponse implements Serializable {

  @Serial
  private static final long serialVersionUID = 2811519614268981995L;
  
  private final Integer id;
  private final Integer productId;
  private final String productName;
  private final VendorResponse vendor;
  private final Boolean requiresPayment;
  private final Boolean showPrice;
  private final Boolean defaultBasePrice;
  private final BigDecimal basePrice;
  private final Boolean defaultVat;
  private final BigDecimal vatPercentage;
  private final BigDecimal vatValue;
  private final Boolean defaultCommission;
  private final BigDecimal commissionPercentage;
  private final BigDecimal commissionValue;
  private final Boolean defaultDescription;
  private final String description;
  private final Boolean defaultMarketing;
  private final String marketing;
  private final Boolean defaultAvailabilityFrom;
  private final LocalDateTime availabilityFrom;
  private final Boolean defaultAvailabilityTo;
  private final LocalDateTime availabilityTo;
  private final BigDecimal totalWithCommission;
  private final BigDecimal totalWithVat;
 /* private final String paymentReference;
  private final Boolean defaultPaymentMethod;
  private final String paymentMethod;
  private final Boolean defaultPaymentDetails;
  private final String paymentDetails;*/
  private final List<FileResponse> files;
}
