package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.martzatech.vdhg.crmprojectback.domains.security.models.DeletedStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
@JsonInclude(Include.NON_EMPTY)
public class CategoryResponse implements Serializable {

  @Serial
  private static final long serialVersionUID = 1202291427913650069L;
  private final Integer id;
  private final String name;
  private final String color;
  private final List<SubCategoryResponse> subCategories;
  private final DeletedStatus deletedStatus;
  private UserResponse creationUser;
  private UserResponse modificationUser;
  private LocalDateTime creationTime;
  private LocalDateTime modificationTime;
}
