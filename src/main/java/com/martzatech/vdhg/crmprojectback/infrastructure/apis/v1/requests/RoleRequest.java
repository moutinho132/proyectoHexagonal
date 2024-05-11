package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests;

import com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants;
import com.martzatech.vdhg.crmprojectback.domains.security.entities.RoleEntity;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@Getter
public class RoleRequest implements Serializable {

  @Serial
  private static final long serialVersionUID = 524597876747347659L;

  private Integer id;

  @NotEmpty(message = CommonConstants.THIS_FIELD_IS_MANDATORY_MESSAGE)
  @Length(message = CommonConstants.MAX_LENGTH_128, max = 128)
  private String name;

  @NotEmpty(message = CommonConstants.THIS_FIELD_IS_MANDATORY_MESSAGE)
  @Length(message = CommonConstants.MAX_LENGTH_512, max = 512)
  private String description;

  @NotNull(message = CommonConstants.THIS_FIELD_IS_MANDATORY_MESSAGE)
  private DepartmentRequest department;

  private RoleEntity role;

  private List<PermissionRequest> permissions;
}
