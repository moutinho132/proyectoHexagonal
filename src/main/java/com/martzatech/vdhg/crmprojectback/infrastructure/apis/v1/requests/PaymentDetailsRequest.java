package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests;

import com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants;
import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@Getter
public class PaymentDetailsRequest implements Serializable {

  @Serial
  private static final long serialVersionUID = -13357557564659827L;

  private Integer id;

  @Length(message = CommonConstants.MAX_LENGTH_128, max = 128)
  private String name;

  @Length(message = CommonConstants.MAX_LENGTH_128, max = 128)
  private String method;

  @Length(message = CommonConstants.MAX_LENGTH_256, max = 256)
  private String reference;
}
