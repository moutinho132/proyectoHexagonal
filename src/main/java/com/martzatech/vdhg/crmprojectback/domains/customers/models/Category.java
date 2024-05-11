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
public class Category {

  private final Integer id;

  @With
  private final String name;

  @With
  private final String color;

  @With
  private final List<SubCategory> subCategories;
  @With
  private final DeletedStatus deleteStatus;
  @With
  private User creationUser;

  @With
  private User modificationUser;

  @With
  private LocalDateTime creationTime;

  @With
  private LocalDateTime modificationTime;
}
