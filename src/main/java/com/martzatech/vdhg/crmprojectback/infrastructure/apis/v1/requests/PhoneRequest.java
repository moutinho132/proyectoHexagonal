package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests;

import com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants;
import jakarta.validation.constraints.NotEmpty;
import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@Getter
public class PhoneRequest implements Serializable {

  @Serial
  private static final long serialVersionUID = 730421852400266650L;

  private Integer id;

  @Length(message = CommonConstants.MAX_LENGTH_8, max = 8)
  @NotEmpty(message = CommonConstants.THIS_FIELD_IS_MANDATORY_MESSAGE)
  private String code;

  @Length(message = CommonConstants.MAX_LENGTH_32, max = 32)
  @NotEmpty(message = CommonConstants.THIS_FIELD_IS_MANDATORY_MESSAGE)
  private String value;

  private AttributeTypeRequest attributeType;
}
