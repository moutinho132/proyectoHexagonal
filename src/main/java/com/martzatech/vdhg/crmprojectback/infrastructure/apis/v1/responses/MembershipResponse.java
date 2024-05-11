package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
@JsonInclude(Include.NON_EMPTY)
public class MembershipResponse implements Serializable {

  @Serial
  private static final long serialVersionUID = 7285765458134825835L;

  private final Integer id;
  private final String name;
  private final MembershipTypeResponse type;
  private final String description;
  private final BigDecimal price;
  private final String features;
  private final Boolean priority;
}
