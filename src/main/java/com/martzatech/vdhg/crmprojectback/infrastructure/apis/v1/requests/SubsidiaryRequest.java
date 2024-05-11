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
public class SubsidiaryRequest implements Serializable {

  @Serial
  private static final long serialVersionUID = -2080646083509296809L;

  private Integer id;

  @Length(message = CommonConstants.MAX_LENGTH_256, max = 256)
  @NotEmpty(message = CommonConstants.THIS_FIELD_IS_MANDATORY_MESSAGE)
  private String name;
}
