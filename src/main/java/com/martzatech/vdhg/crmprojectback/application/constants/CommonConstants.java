package com.martzatech.vdhg.crmprojectback.application.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonConstants {

  public static final String PERSON_TITLE_ID = "person.title.id";

  public static final String PERSON = "person";

  public static final String LEAD_COMPANY_FIELD = "company.(id|name)";

  public static final String EMAILS = "person.emails";

  public static final String IDENTITY_DOCUMENTS = "person.identityDocuments";

  public static final String PHONES = "person.phones";

  public static final String ADDRESSES = "person.addresses";

  public static final String TYPE = "type";

  public static final String IDENTITY_DOCUMENT_TYPE = "person.identityDocuments.type.id";

  public static final String MAIN_CONTACT_FIELD = "mainContact.id";

  public static final String OWNER_FIELD = "owner.id";

  public static final String OFFER_ID = "offer.id";

  public static final String CUSTOMERS_FIELD = "customers[%d].id";

  public static final String NAME_FIELD = "name";

  public static final String MEMBERSHIP = "membership";

  public static final String RESIDENCE = "person.residence";

  public static final String SUBSIDIARY_FIELD = "subsidiary";

  public static final String DEPARTMENT_FIELD = "department";

  public static final String EMAIL_FIELD = "email";

  public static final String ROLES_FIELD = "roles";

  public static final String THIS_FIELD_IS_MANDATORY_MESSAGE = "This field is mandatory";

  public static final String THIS_FIELD_REQUEST_ERROR_FIELD= "The value must be between 0.00 and 100.00";


  public static final String IS_A_CUSTOMER_MESSAGE = "Cannot delete because the lead is already a customer";

  public static final String WHEN_UPDATE_THE_PERSON_ID_IS_MANDATORY_MESSAGE = "When update the person id is mandatory";

  public static final String WHEN_CREATE_THE_LEAD_WITH_EXISTING_PERSON = "Cannot create a lead with an existing person";

  public static final String PERSON_ID_DOES_NOT_CORRESPOND_MESSAGE = "Person id does not correspond to the parent object";

  public static final String THIS_FIELD_IS_DUPLICATED = "This field value is duplicated";

  public static final String MAX_LENGTH_4 = "The maximum length allowed is 4";

  public static final String MAX_LENGTH_8 = "The maximum length allowed is 8";

  public static final String MAX_LENGTH_32 = "The maximum length allowed is 32";

  public static final String MAX_LENGTH_64 = "The maximum length allowed is 64";

  public static final String MAX_LENGTH_128 = "The maximum length allowed is 128";

  public static final String MAX_LENGTH_256 = "The maximum length allowed is 256";

  public static final String MAX_LENGTH_512 = "The maximum length allowed is 512";

  public static final String MAX_LENGTH_1024 = "The maximum length allowed is 1024";

  public static final String NOT_VALID_EMAIL = "This value is not a valid email";

  public static final String DECIMAL_MIN_MESSAGE = "Min allowed 0.0";

  public static final String DECIMAL_MIN_MESSAGE_DEC = "Min allowed 00.00";
  public static final String EXPECTED_DIGITS_MESSAGE = "Format expected 999999999.99 (11,2)";

  public static final String DECIMAL_MAX_MESSAGE = "Max allowed 0.0";
  public static final String DECIMAL_MAX_MESSAGE_DEC = "Max allowed 100.00";
  public static final String EMAIL_MS_KEY = "upn";

  public static final String AT_LEAST_ONE_IS_REQUIRED = "At least one is required";

  public static final String NOT_VALID_DISCOUNT_TYPE = "Should be PERCENTAGE or FIXED";

  public static final String STATUS_FIELD = "active";

  public static final String AZURE_PATH = "products";
  public static final String EXTENSION_REGEX = "^[a-z]{1,5}[a-z0-9]$";
  public static final String EXTENSION_REGEX_DOC = "^.*\\.(png|PNG|jpeg|JPEG|gif|GIF|webp|WEBP|pdf|PDF|docx|DOCX|txt|TXT|xlsx|XLSX|xls|XLS)$";

}
