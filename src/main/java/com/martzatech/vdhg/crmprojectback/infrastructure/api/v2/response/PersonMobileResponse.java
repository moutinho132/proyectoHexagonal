package com.martzatech.vdhg.crmprojectback.infrastructure.api.v2.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Getter
@Builder
@JsonInclude(Include.NON_EMPTY)
public class PersonMobileResponse implements Serializable {

  @Serial
  private static final long serialVersionUID = 4013604383803994035L;

  private final Integer id;
  private final String name;
  private final String surname;
  private final PersonTitleResponse title;

}
