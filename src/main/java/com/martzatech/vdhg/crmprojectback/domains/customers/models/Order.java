package com.martzatech.vdhg.crmprojectback.domains.customers.models;

import com.martzatech.vdhg.crmprojectback.domains.customers.enums.OrderStatusEnum;
import com.martzatech.vdhg.crmprojectback.domains.security.models.DeletedStatus;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
public class Order {

  private final Integer id;

  @With
  private OrderStatusEnum status;

  @With
  private final String notes;
  @With
  private String description;

  @With
  private final Offer offer;
  @With
  private String pdfUrl;
  @With
  private String pdfUrlInternal;

  @With
  private final PaymentDetails paymentDetails;
  /*@With
  private Boolean restricted;*/

  @With
  private final DeletedStatus deletedStatus;

  @With
  private final User creationUser;

  @With
  private final User modificationUser;

  @With
  private final LocalDateTime creationTime;

  @With
  private final LocalDateTime modificationTime;
}
