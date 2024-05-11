package com.martzatech.vdhg.crmprojectback.domains.customers.models;

import com.martzatech.vdhg.crmprojectback.domains.commons.models.Person;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class Employee {

  private final Integer id;
  private final LocalDateTime creationTime;
  private final EmployeeType type;
  private final Person person;
}
