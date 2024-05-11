package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests;

import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CountryRequest implements Serializable {

  @Serial
  private static final long serialVersionUID = 3044864231551056275L;

  private Integer id;
}
