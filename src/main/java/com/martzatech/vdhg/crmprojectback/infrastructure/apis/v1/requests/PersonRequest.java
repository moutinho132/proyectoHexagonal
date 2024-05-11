package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests;

import com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PersonRequest implements Serializable {

  @Serial
  private static final long serialVersionUID = -4276196622211080058L;

  private Integer id;

  @NotEmpty(message = CommonConstants.THIS_FIELD_IS_MANDATORY_MESSAGE)
  private String name;

  @NotEmpty(message = CommonConstants.THIS_FIELD_IS_MANDATORY_MESSAGE)
  private String surname;

  private LocalDate dateOfBirth;

  @NotNull(message = CommonConstants.THIS_FIELD_IS_MANDATORY_MESSAGE)
  private PersonTitleRequest title;

  private CountryRequest nationality;

  private CountryRequest residence;

  private GenderRequest gender;

  private CivilStatusRequest civilStatus;

  private LanguageRequest preferredLanguage;

  @Valid
  private List<IdentityDocumentRequest> identityDocuments;

  @Valid
  private List<PhoneRequest> phones;

  @Valid
  private List<EmailRequest> emails;

  @Valid
  private List<AddressRequest> addresses;
}
