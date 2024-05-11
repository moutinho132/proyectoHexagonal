package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests;

import com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class EmailRequest implements Serializable {

  @Serial
  private static final long serialVersionUID = -1988493847052708457L;

  private Integer id;

  private Boolean valid;

  @NotEmpty(message = CommonConstants.THIS_FIELD_IS_MANDATORY_MESSAGE)
  @Pattern(
      regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$",
      message = CommonConstants.NOT_VALID_EMAIL
  )
  private String value;

  private AttributeTypeRequest attributeType;
}
