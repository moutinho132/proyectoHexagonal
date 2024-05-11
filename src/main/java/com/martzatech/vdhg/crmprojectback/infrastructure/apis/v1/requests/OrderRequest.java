package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests;

import com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;

@NoArgsConstructor
@Getter
public class OrderRequest implements Serializable {

  @Serial
  private static final long serialVersionUID = -5394408699823449457L;

  private Integer id;

  @Length(message = CommonConstants.MAX_LENGTH_512, max = 512)
  private String notes;

  private String description;

  @NotNull(message = CommonConstants.THIS_FIELD_IS_MANDATORY_MESSAGE)
  private OfferRequest offer;

  @Valid
  private PaymentDetailsRequest paymentDetails;

  private Boolean restricted;
}
