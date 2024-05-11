package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests;

import com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants;
import jakarta.validation.constraints.NotEmpty;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@Getter
public class NoteRequest implements Serializable {

  @Serial
  private static final long serialVersionUID = -5544963071852104317L;

  private Integer id;

  @NotEmpty(message = CommonConstants.THIS_FIELD_IS_MANDATORY_MESSAGE)
  @Length(message = CommonConstants.MAX_LENGTH_256, max = 256)
  private String title;

  @NotEmpty(message = CommonConstants.THIS_FIELD_IS_MANDATORY_MESSAGE)
  @Length(message = CommonConstants.MAX_LENGTH_1024, max = 1024)
  private String description;

  private List<RoleRequest> roles;

  private List<UserRequest> users;
}
