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
public class OfferMobileResponse implements Serializable {

  @Serial
  private static final long serialVersionUID = 8327936974612698875L;

  private final Integer id;
  private final Integer number;
  private final Integer version;
 // private final OfferStatusEnum status;
 //private final OfferGLobalStatusEnum globalStatus;
  private final String pdfUrl;
  private final String textToClient;
  private final String name;
  private final PreOfferResponseNew preOffer;
  private final String description;
 // private final CustomerResponse customer;
  private final List<ProductOfferPriceBokingsMobileResponse> products;
  private final List<FileResponse> files;
  private Boolean defaultExpiration;
  private final String currency;
  private final BigDecimal subtotal;
  private final BigDecimal total;
  private final DiscountResponse discount;
  private final Boolean paymentRequired;
  private final LocalDateTime expirationTime;
  private final Boolean restricted;
  private final UserResponse creationUser;
  private final UserResponse modificationUser;
  private final LocalDateTime creationTime;
  private final LocalDateTime modificationTime;
}
