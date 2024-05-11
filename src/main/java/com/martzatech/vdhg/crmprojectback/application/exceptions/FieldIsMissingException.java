package com.martzatech.vdhg.crmprojectback.application.exceptions;

import com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants;
import java.io.Serial;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FieldIsMissingException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = -8433363895084938019L;

  private final String name;
  private final String message = CommonConstants.THIS_FIELD_IS_MANDATORY_MESSAGE;
}
