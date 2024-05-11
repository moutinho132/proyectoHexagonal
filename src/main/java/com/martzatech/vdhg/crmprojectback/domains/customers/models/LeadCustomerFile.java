package com.martzatech.vdhg.crmprojectback.domains.customers.models;

import com.martzatech.vdhg.crmprojectback.domains.chat.models.File;
import com.martzatech.vdhg.crmprojectback.domains.commons.models.Person;
import com.martzatech.vdhg.crmprojectback.domains.security.models.DeletedStatus;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.FileResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class LeadCustomerFile {

  private final Integer id;

  private final String url;

  private final String name;

  private final Lead lead;

  private final Customer customer;

  private final Person person;

  private final User creationUser;

  @With
  private final List<FileResponse> files;

  @With
  private final DeletedStatus status;

  @With
  private final User removalUser;

  private final LocalDateTime creationTime;

  @With
  private final LocalDateTime removalTime;
}
