package com.martzatech.vdhg.crmprojectback.domains.customers.models;

import com.martzatech.vdhg.crmprojectback.domains.security.models.DeletedStatus;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;

@AllArgsConstructor
@Getter
@Builder
public class Corporate {

  private final Integer id;

  @With
  private final Customer mainContact;

  @With
  private final List<Customer> customers;

  @With
  private final String name;

  @With
  private final String alias;

  @With
  private final String industry;

  @With
  private final String vat;

  @With
  private String email;

  @With
  private final DeletedStatus status;

  @With
  private User creationUser;

  @With
  private User modificationUser;

  @With
  private final LocalDateTime creationTime;

  @With
  private final LocalDateTime modificationTime;
}
