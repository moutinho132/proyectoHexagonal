package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests;

import com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class OfferRequest implements Serializable {

  @Serial
  private static final long serialVersionUID = -5953929409870391762L;

  private Integer id;

  private Integer number;

  private PreOfferRequest preOffer;

  @NotEmpty(message = CommonConstants.THIS_FIELD_IS_MANDATORY_MESSAGE)
  @Length(message = CommonConstants.MAX_LENGTH_256, max = 256)
  private String name;

  @Length(message = CommonConstants.MAX_LENGTH_512, max = 512)
  private String description;

  @NotEmpty(message = CommonConstants.THIS_FIELD_IS_MANDATORY_MESSAGE)
  private String textToClient;

  @NotNull(message = CommonConstants.THIS_FIELD_IS_MANDATORY_MESSAGE)
  private CustomerRequest customer;

  /*@NotEmpty(message = CommonConstants.AT_LEAST_ONE_IS_REQUIRED)
  @Valid
  private List<ProductPriceRequest> products;*/

  @Length(message = CommonConstants.MAX_LENGTH_8, max = 8)
  private String currency;

  @DecimalMin(value = "0.0", message = CommonConstants.DECIMAL_MIN_MESSAGE)
  @Digits(integer = 9, fraction = 2, message = CommonConstants.EXPECTED_DIGITS_MESSAGE)
  @DecimalMax(value = "999999999.99", message = CommonConstants.DECIMAL_MAX_MESSAGE)
  private BigDecimal subtotal;

  /*@Valid
  private DiscountRequest discount;**/

  //private Boolean paymentRequired;
  private Boolean defaultExpiration;
  private LocalDateTime expirationTime;
  private Boolean restricted;
}
