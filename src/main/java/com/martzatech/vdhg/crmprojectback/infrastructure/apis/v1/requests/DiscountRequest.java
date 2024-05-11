package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests;

import com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Pattern;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class DiscountRequest implements Serializable {

  @Serial
  private static final long serialVersionUID = 8958181013213863562L;

  private Integer id;

  @Pattern(
      regexp = "^(PERCENTAGE|FIXED){1}$",
      message = CommonConstants.NOT_VALID_DISCOUNT_TYPE
  )
  private String type;

  @DecimalMin(value = "0.0", message = CommonConstants.DECIMAL_MIN_MESSAGE)
  @Digits(integer = 9, fraction = 2, message = CommonConstants.EXPECTED_DIGITS_MESSAGE)
  @DecimalMax(value = "999999999.99", message = CommonConstants.DECIMAL_MAX_MESSAGE)
  private BigDecimal value;
}
