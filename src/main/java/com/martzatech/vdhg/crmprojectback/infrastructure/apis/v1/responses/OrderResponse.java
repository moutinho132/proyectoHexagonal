package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.martzatech.vdhg.crmprojectback.domains.customers.enums.OrderStatusEnum;
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
public class OrderResponse implements Serializable {

  @Serial
  private static final long serialVersionUID = 5948227642437574925L;

  private final Integer id;
  private final OrderStatusEnum status;
  private final String notes;
  private final String pdfUrl;
  private final String pdfUrlInternal;
  private final String description;
  private final OfferResponse offer;
  private final PaymentDetailsResponse paymentDetails;
  private final Boolean restricted;
  private final UserResponse creationUser;
  private final UserResponse modificationUser;
  private final LocalDateTime creationTime;
  private final LocalDateTime modificationTime;
}
