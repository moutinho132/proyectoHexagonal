package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests;

import com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@Getter
public class UserRequest implements Serializable {

  @Serial
  private static final long serialVersionUID = -8618174661704027859L;

  private Integer id;
  
  @NotEmpty(message = CommonConstants.THIS_FIELD_IS_MANDATORY_MESSAGE)
  @Length(message = CommonConstants.MAX_LENGTH_4, max = 4)
  private String title;

  @NotEmpty(message = CommonConstants.THIS_FIELD_IS_MANDATORY_MESSAGE)
  @Length(message = CommonConstants.MAX_LENGTH_128, max = 128)
  private String name;

  @NotEmpty(message = CommonConstants.THIS_FIELD_IS_MANDATORY_MESSAGE)
  @Length(message = CommonConstants.MAX_LENGTH_128, max = 128)
  private String surname;

  @NotEmpty(message = CommonConstants.THIS_FIELD_IS_MANDATORY_MESSAGE)
  @Pattern(
      regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$",
      message = CommonConstants.NOT_VALID_EMAIL
  )
  @Length(message = CommonConstants.MAX_LENGTH_128, max = 128)
  private String email;

  @Length(message = CommonConstants.MAX_LENGTH_128, max = 32)
  private String mobile;

  @Length(message = CommonConstants.MAX_LENGTH_512, max = 512)
  private String address;

  @Length(message = CommonConstants.MAX_LENGTH_4, max = 4)
  private String nationality;

  @NotEmpty(message = CommonConstants.THIS_FIELD_IS_MANDATORY_MESSAGE)
  private List<RoleRequest> roles;
}
