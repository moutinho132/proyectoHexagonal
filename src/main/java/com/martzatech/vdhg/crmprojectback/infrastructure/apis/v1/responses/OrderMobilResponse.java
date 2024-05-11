package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
@JsonInclude(Include.NON_EMPTY)
public class OrderMobilResponse implements Serializable {

  @Serial
  private static final long serialVersionUID = 5948227642437574925L;

  private final Integer id;
  private final String notes;
  private final String pdfUrl;
  private final String description;
  private final OfferMobileResponse offer;
  private final PaymentDetailsResponse paymentDetails;
  private final Boolean restricted;
  private final UserResponse creationUser;
  private final UserResponse modificationUser;
  private final LocalDateTime creationTime;
  private final LocalDateTime modificationTime;
}
