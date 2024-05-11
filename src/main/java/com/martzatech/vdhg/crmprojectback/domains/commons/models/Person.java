package com.martzatech.vdhg.crmprojectback.domains.commons.models;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;

@AllArgsConstructor
@Getter
@Builder
public class Person {

  private final Integer id;

  @With
  private final String name;

  @With
  private final String surname;

  private final LocalDate dateOfBirth;

  @With
  private final PersonTitle title;

  @With
  private final Country nationality;

  private final Country residence;

  private final Gender gender;

  private final CivilStatus civilStatus;

  private final Language preferredLanguage;

  @With
  private final List<IdentityDocument> identityDocuments;

  @With
  private final List<Phone> phones;

  @With
  private final List<Email> emails;

  @With
  private final List<Address> addresses;
}
