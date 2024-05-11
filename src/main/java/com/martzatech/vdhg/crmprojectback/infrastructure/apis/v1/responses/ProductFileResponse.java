package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.martzatech.vdhg.crmprojectback.domains.customers.models.Product;
import com.martzatech.vdhg.crmprojectback.domains.security.models.DeletedStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;

@AllArgsConstructor
@Getter
@Builder
@JsonInclude(Include.NON_EMPTY)
public class ProductFileResponse implements Serializable {

  @Serial
  private static final long serialVersionUID = 4588746633794586540L;

  private final Integer id;
  private final String url;
  private final String name;
  @With
  private final String extension;
  @With
  private List<Product> products;

  private final UserResponse creationUser;
  private final LocalDateTime creationTime;
}
