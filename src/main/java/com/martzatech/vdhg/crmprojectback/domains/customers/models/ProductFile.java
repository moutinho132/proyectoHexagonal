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
public class ProductFile {

  private final Integer id;

  private final String url;

  private final String name;
  @With
  private final String extension;

  @With
  private List<Product> products;

  private final User creationUser;

  @With
  private final DeletedStatus status;

  @With
  private final User removalUser;

  private final LocalDateTime creationTime;

  @With
  private final LocalDateTime removalTime;
}
