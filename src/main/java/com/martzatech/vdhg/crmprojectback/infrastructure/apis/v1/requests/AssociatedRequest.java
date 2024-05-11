package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests;

import com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@Getter
public class AssociatedRequest implements Serializable {

  @Serial
  private static final long serialVersionUID = -7951179330069372844L;

  private Integer id;

  @NotEmpty(message = CommonConstants.THIS_FIELD_IS_MANDATORY_MESSAGE)
  @Length(message = CommonConstants.MAX_LENGTH_256, max = 256)
  private String name;

  @NotEmpty(message = CommonConstants.THIS_FIELD_IS_MANDATORY_MESSAGE)
  @Length(message = CommonConstants.MAX_LENGTH_256, max = 256)
  private String surname;

  @Pattern(
      regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$",
      message = CommonConstants.NOT_VALID_EMAIL
  )
  @NotEmpty(message = CommonConstants.THIS_FIELD_IS_MANDATORY_MESSAGE)
  @Length(message = CommonConstants.MAX_LENGTH_256, max = 256)
  private String email;

  @NotEmpty(message = CommonConstants.THIS_FIELD_IS_MANDATORY_MESSAGE)
  @Length(message = CommonConstants.MAX_LENGTH_256, max = 256)
  private String position;

  @Length(message = CommonConstants.MAX_LENGTH_8, max = 8)
  private String phonePrefix;

  @Length(message = CommonConstants.MAX_LENGTH_32, max = 32)
  private String phoneNumber;

  private LanguageRequest preferredLanguage;

  private  final Boolean mainContact = Boolean.FALSE;
}
