package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.martzatech.vdhg.crmprojectback.domains.security.models.DeletedStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
@JsonInclude(Include.NON_EMPTY)
public class LeadResponse implements Serializable {

  @Serial
  private static final long serialVersionUID = 7864677618769518477L;

  private final Integer id;
  private final LeadStatusResponse status;
  private final RegistrationTypeResponse registrationType;
  private final PersonResponse person;
  private final CustomerResponse referringCustomer;
  private final CompanyResponse company;
  private final LocalDateTime creationTime;
  private final LocalDateTime modificationTime;
  private final String reference;
}
