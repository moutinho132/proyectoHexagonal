package com.martzatech.vdhg.crmprojectback.domains.customers.models;

import com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants;
import com.martzatech.vdhg.crmprojectback.domains.customers.enums.OfferStatusEnum;
import com.martzatech.vdhg.crmprojectback.domains.customers.enums.PreOfferStatusEnum;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
@Builder
@Setter
public class PreOffer {

  private Integer id;

  @With
  private String name;

  @With
  private Integer number;

  @With
  private Integer version;

  @With
  private String textToClient;

  @With
  private OfferStatusEnum status;

  @With
  private PreOfferStatusEnum globalStatus;

  @With
  private String description;

  @With
  private List<BockingProduct> products;

  @With
  private String currency;

  @With
  @DecimalMin(value = "0.0", message = CommonConstants.DECIMAL_MIN_MESSAGE)
  @Digits(integer = 9, fraction = 2, message = CommonConstants.EXPECTED_DIGITS_MESSAGE)
  @DecimalMax(value = "999999999.99", message = CommonConstants.DECIMAL_MAX_MESSAGE)
  private BigDecimal subtotal;

  @With
  @DecimalMin(value = "0.0", message = CommonConstants.DECIMAL_MIN_MESSAGE)
  @Digits(integer = 9, fraction = 2, message = CommonConstants.EXPECTED_DIGITS_MESSAGE)
  @DecimalMax(value = "999999999.99", message = CommonConstants.DECIMAL_MAX_MESSAGE)
  private BigDecimal total;

  @With
  private Discount discount;

  @With
  private Boolean paymentRequired;

  @With
  private LocalDateTime expirationTime;

  private Boolean active;

  @With
  private String pdfUrl;

  @With
  private User creationUser;
  @With
  private Boolean defaultExpiration;

  @With
  private User modificationUser;

  @With
  private LocalDateTime creationTime;

  @With
  private LocalDateTime modificationTime;
}
