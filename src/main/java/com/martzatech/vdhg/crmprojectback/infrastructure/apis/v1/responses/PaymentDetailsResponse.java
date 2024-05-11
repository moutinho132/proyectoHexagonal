package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
@JsonInclude(Include.NON_EMPTY)
public class PaymentDetailsResponse implements Serializable {

  @Serial
  private static final long serialVersionUID = 6167192420785503383L;

  private final Integer id;
  private final String name;
  private final String method;
  private final String reference;
}
