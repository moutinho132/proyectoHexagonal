package com.martzatech.vdhg.crmprojectback.application.exceptions;

import com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants;
import java.io.Serial;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FieldIsDuplicatedException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = 5235121069078709566L;

  private final String name;
  private final String message = CommonConstants.THIS_FIELD_IS_DUPLICATED;
}
