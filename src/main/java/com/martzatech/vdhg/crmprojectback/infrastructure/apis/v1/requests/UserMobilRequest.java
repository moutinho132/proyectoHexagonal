package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests;

import com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;

@NoArgsConstructor
@Getter
public class UserMobilRequest implements Serializable {

  @Serial
  private static final long serialVersionUID = -8618174661704027859L;
  @NotEmpty(message = CommonConstants.THIS_FIELD_IS_MANDATORY_MESSAGE)
  @Length(message = CommonConstants.MAX_LENGTH_128, max = 128)
  private String userName;


  @NotEmpty(message = CommonConstants.THIS_FIELD_IS_MANDATORY_MESSAGE)
  @Length(message = CommonConstants.MAX_LENGTH_128, max = 128)
  private String password;

}
