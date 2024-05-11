package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests;

import com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@Getter
public class GroupAccountRequest implements Serializable {

  @Serial
  private static final long serialVersionUID = -7912609507285863035L;

  private Integer id;

  @NotEmpty(message = CommonConstants.THIS_FIELD_IS_MANDATORY_MESSAGE)
  @Length(message = CommonConstants.MAX_LENGTH_256, max = 256)
  private String name;

  @Pattern(
      regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$",
      message = CommonConstants.NOT_VALID_EMAIL
  )
  @Length(message = CommonConstants.MAX_LENGTH_256, max = 256)
  private String email;

  @Length(message = CommonConstants.MAX_LENGTH_256, max = 256)
  private String industry;

  @Length(message = CommonConstants.MAX_LENGTH_256, max = 256)
  private String vat;

  @Length(message = CommonConstants.MAX_LENGTH_256, max = 256)
  private String alias;

  @NotNull(message = CommonConstants.THIS_FIELD_IS_MANDATORY_MESSAGE)
  private CustomerRequest owner;
}
