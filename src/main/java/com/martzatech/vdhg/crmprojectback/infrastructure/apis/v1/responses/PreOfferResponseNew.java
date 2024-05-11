package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

@AllArgsConstructor
@Getter
@Builder
@JsonInclude(Include.NON_EMPTY)
public class PreOfferResponseNew implements Serializable {

  @Serial
  private static final long serialVersionUID = 2632452407576927120L;

  private final Integer id;
  private final String name;
  private final Integer number;
  private final Integer version;

}
