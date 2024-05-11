package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests;

import com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CustomerRequest implements Serializable {

  @Serial
  private static final long serialVersionUID = 614015548881635883L;

  private Integer id;

  private String alias;

  @NotNull(message = CommonConstants.THIS_FIELD_IS_MANDATORY_MESSAGE)
  private MembershipRequest membership;

  private CompanyRequest company;

  @Valid
  private PaymentDetailsRequest paymentDetails;

  @Valid
  @NotNull(message = CommonConstants.THIS_FIELD_IS_MANDATORY_MESSAGE)
  private PersonRequest person;

  private String reference;

  private Integer loyaltyPoints;
}
