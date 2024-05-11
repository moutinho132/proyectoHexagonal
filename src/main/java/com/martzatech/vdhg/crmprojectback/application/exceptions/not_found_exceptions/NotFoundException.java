package com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions;

import java.io.Serial;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class NotFoundException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = 4620034341502172002L;

  private Integer id;
  private String resource;
}
