package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests;

import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserStatusRequest implements Serializable {

  private Integer id;
  private String name;
}
