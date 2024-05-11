package com.martzatech.vdhg.crmprojectback.domains.customers.models;

import com.martzatech.vdhg.crmprojectback.domains.commons.models.Person;
import java.time.LocalDateTime;

import com.martzatech.vdhg.crmprojectback.domains.security.models.DeletedStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;

@AllArgsConstructor
@Getter
@Builder
public class Lead {

  private final Integer id;

  @With
  private final LeadStatus status;

  @With
  private final RegistrationType registrationType;

  @With
  private final Person person;

  @With
  private final Customer referringCustomer;

  @With
  private final String reference;
  @With
  private final Company company;

  @With
  private final DeletedStatus deletedStatus;

  @With
  private final LocalDateTime creationTime;

  @With
  private final LocalDateTime modificationTime;
}
