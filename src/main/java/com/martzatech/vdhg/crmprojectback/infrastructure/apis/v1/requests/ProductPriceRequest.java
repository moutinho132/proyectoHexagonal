package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests;

import com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ProductPriceRequest implements Serializable {

  @Serial
  private static final long serialVersionUID = -2709415540705910877L;

  private Integer id;

  @DecimalMin(value = "0.0", message = CommonConstants.DECIMAL_MIN_MESSAGE)
  @Digits(integer = 9, fraction = 2, message = CommonConstants.EXPECTED_DIGITS_MESSAGE)
  @DecimalMax(value = "999999999.99", message = CommonConstants.DECIMAL_MAX_MESSAGE)
  private BigDecimal price;

  @NotNull(message = CommonConstants.THIS_FIELD_IS_MANDATORY_MESSAGE)
  private ProductRequest product;
}
