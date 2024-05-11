package com.martzatech.vdhg.crmprojectback.domains.security.models;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class Privilege {

  private final Integer id;
  private final String name;
  private final String description;
  private final List<Permission> permissions;
}
