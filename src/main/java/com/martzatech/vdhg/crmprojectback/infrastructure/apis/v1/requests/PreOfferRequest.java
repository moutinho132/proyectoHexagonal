package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests;

import com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
public class PreOfferRequest implements Serializable {

  @Serial
  private static final long serialVersionUID = -1006121430393994685L;

  private Integer id;

  private Integer number;

  @NotEmpty(message = CommonConstants.THIS_FIELD_IS_MANDATORY_MESSAGE)
  @Length(message = CommonConstants.MAX_LENGTH_256, max = 256)
  private String name;

  @Length(message = CommonConstants.MAX_LENGTH_512, max = 512)
  private String description;

  @NotEmpty(message = CommonConstants.THIS_FIELD_IS_MANDATORY_MESSAGE)
  private String textToClient;

 // @NotEmpty(message = CommonConstants.AT_LEAST_ONE_IS_REQUIRED)
 // @Valid
  private List<ProductPriceRequest> products;

  @Length(message = CommonConstants.MAX_LENGTH_8, max = 8)
  private String currency;

  private BigDecimal subtotal;

  @Valid
  private DiscountRequest discount;

  private Boolean defaultExpiration;

  private Boolean paymentRequired;

  private LocalDateTime expirationTime;
}
