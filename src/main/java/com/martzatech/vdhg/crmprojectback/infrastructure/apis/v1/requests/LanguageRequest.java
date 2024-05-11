package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests;

import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class LanguageRequest implements Serializable {

  @Serial
  private static final long serialVersionUID = -8622266672814335388L;

  private Integer id;
}
