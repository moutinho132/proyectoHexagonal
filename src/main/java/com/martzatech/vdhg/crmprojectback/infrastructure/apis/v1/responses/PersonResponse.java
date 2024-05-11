package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
@JsonInclude(Include.NON_EMPTY)
public class PersonResponse implements Serializable {

  @Serial
  private static final long serialVersionUID = 4013604383803994035L;

  private final Integer id;
  private final String name;
  private final String surname;
  private final LocalDate dateOfBirth;
  private final PersonTitleResponse title;
  private final CountryResponse nationality;
  private final CountryResponse residence;
  private final GenderResponse gender;
  private final CivilStatusResponse civilStatus;
  private final LanguageResponse preferredLanguage;
  private final List<IdentityDocumentResponse> identityDocuments;
  private final List<PhoneResponse> phones;
  private final List<EmailResponse> emails;
  private final List<AddressResponse> addresses;
}
