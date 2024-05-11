package com.martzatech.vdhg.crmprojectback.domains.customers.models;

import com.martzatech.vdhg.crmprojectback.domains.chat.models.ChatRoom;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.File;
import com.martzatech.vdhg.crmprojectback.domains.commons.models.Person;
import com.martzatech.vdhg.crmprojectback.domains.security.models.DeletedStatus;
import com.martzatech.vdhg.crmprojectback.domains.security.models.Role;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;

@AllArgsConstructor
@Getter
@Builder
public class Customer {

  private final Integer id;

  @With
  private final String alias;

  @With
  private final Membership membership;

  @With
  private final Company company;

  @With
  private final Person person;

  @With
  private final String reference;

  private final Integer loyaltyPoints;

  @With
  private final Lead lead;

  @With
  private final CustomerStatus status;

  @With
  private final List<Role> roles;
  @With
  private final List<File> files;

  @With
  private final ChatRoom chatRoom;

  @With
  private final DeletedStatus deletedStatus;

  @With
  private final CreationType creationType;

  @With
  private final PaymentDetails paymentDetails;

  private final GroupAccount groupAccount;

  private final BigDecimal totalSpendAmount;

  @With
  private final User creationUser;

  @With
  private final User modificationUser;

  @With
  private final LocalDateTime creationTime;

  @With
  private final LocalDateTime modificationTime;
}
