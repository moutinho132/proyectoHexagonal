package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests;

import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CompanyRequest implements Serializable {

  @Serial
  private static final long serialVersionUID = -3513754870263404276L;

  private Integer id;
  private String name;
}
