package com.martzatech.vdhg.crmprojectback.application.services;

import com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants;
import com.martzatech.vdhg.crmprojectback.application.exceptions.FieldIsMissingException;
import com.martzatech.vdhg.crmprojectback.domains.commons.models.Address;
import com.martzatech.vdhg.crmprojectback.domains.commons.models.AttributeType;
import com.martzatech.vdhg.crmprojectback.domains.commons.models.CivilStatus;
import com.martzatech.vdhg.crmprojectback.domains.commons.models.Country;
import com.martzatech.vdhg.crmprojectback.domains.commons.models.Email;
import com.martzatech.vdhg.crmprojectback.domains.commons.models.IdentityDocument;
import com.martzatech.vdhg.crmprojectback.domains.commons.models.IdentityDocumentType;
import com.martzatech.vdhg.crmprojectback.domains.commons.models.Language;
import com.martzatech.vdhg.crmprojectback.domains.commons.models.Person;
import com.martzatech.vdhg.crmprojectback.domains.commons.models.PersonTitle;
import com.martzatech.vdhg.crmprojectback.domains.commons.models.Phone;
import com.martzatech.vdhg.crmprojectback.domains.commons.services.AddressService;
import com.martzatech.vdhg.crmprojectback.domains.commons.services.AttributeTypeService;
import com.martzatech.vdhg.crmprojectback.domains.commons.services.CivilStatusService;
import com.martzatech.vdhg.crmprojectback.domains.commons.services.CountryService;
import com.martzatech.vdhg.crmprojectback.domains.commons.services.EmailService;
import com.martzatech.vdhg.crmprojectback.domains.commons.services.IdentityDocumentService;
import com.martzatech.vdhg.crmprojectback.domains.commons.services.IdentityDocumentTypeService;
import com.martzatech.vdhg.crmprojectback.domains.commons.services.LanguageService;
import com.martzatech.vdhg.crmprojectback.domains.commons.services.PersonService;
import com.martzatech.vdhg.crmprojectback.domains.commons.services.PersonTitleService;
import com.martzatech.vdhg.crmprojectback.domains.commons.services.PhoneService;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@AllArgsConstructor
@Slf4j
@Service
public class PersonManagementService {

  private final PersonService personService;
  private final IdentityDocumentService identityDocumentService;
  private final IdentityDocumentTypeService identityDocumentTypeService;
  private final PhoneService phoneService;
  private final EmailService emailService;
  private final AddressService addressService;
  private final AttributeTypeService attributeTypeService;
  private final CountryService countryService;
  private final PersonTitleService personTitleService;
  private final CivilStatusService civilStatusService;
  private final LanguageService languageService;

  @Transactional
  public Person save(final Person model) {
    validations(model);

    return personService.save(build(model));
  }

  /*
   * Validators
   */
  private void  validations(final Person model) {
    validId(model.getId());
    validPersonTitle(model.getTitle());
    validCivilStatus(model.getCivilStatus());
    validCountry(model.getNationality());
    validCountry(model.getResidence());
    validLanguage(model.getPreferredLanguage());
    validCountriesInAddresses(model.getAddresses());
  }

  private void validCountriesInAddresses(final List<Address> addresses) {
    if (!CollectionUtils.isEmpty(addresses)) {
      for (final Address address : addresses) {
        validCountry(address.getCountry());
      }
    }
  }

  private void validLanguage(final Language model) {
    if (Objects.nonNull(model) && Objects.nonNull(model.getId())) {
      languageService.existsById(model.getId());
    }
  }

  private void validId(final Integer id) {
    if (Objects.nonNull(id)) {
      personService.existsById(id);
    }
  }

  private void validPersonTitle(final PersonTitle model) {
    if (Objects.isNull(model) || Objects.isNull(model.getId())) {
      throw new FieldIsMissingException(CommonConstants.PERSON_TITLE_ID);
    }
    personTitleService.existsById(model.getId());
  }

  private void validCivilStatus(final CivilStatus model) {
    if (Objects.nonNull(model) && Objects.nonNull(model.getId())) {
      civilStatusService.existsById(model.getId());
    }
  }

  private void validCountry(final Country model) {
    if (Objects.nonNull(model) && Objects.nonNull(model.getId())) {
      countryService.existsById(model.getId());
    }
  }

  private void validIdentityDocumentType(final IdentityDocumentType model) {
    if (Objects.isNull(model) || Objects.isNull(model.getId())) {
      throw new FieldIsMissingException(CommonConstants.IDENTITY_DOCUMENT_TYPE);
    }
    identityDocumentTypeService.findById(model.getId());
  }

  /*
   * Builders
   */

  private Person build(final Person model) {
    final AtomicReference<Person> built = new AtomicReference<>(generateId(model));
    built.set(buildIdentityDocuments(built.get()));
    built.set(buildPhones(built.get()));
    built.set(buildEmails(built.get()));
    built.set(buildAddresses(built.get()));
    return built.get();
  }

  private Person generateId(final Person model) {
    if (Objects.isNull(model.getId())) {
      return personService
          .save(model)
          .withIdentityDocuments(model.getIdentityDocuments())
          .withPhones(model.getPhones())
          .withEmails(model.getEmails())
          .withAddresses(model.getAddresses());
    }
    return model;
  }

  private Person buildIdentityDocuments(final Person model) {
    if (Objects.nonNull(model.getIdentityDocuments())) {
      final List<Integer> idsToKeep = keepOnlyIdentityDocumentsExists(model);
      return model.withIdentityDocuments(
          model.getIdentityDocuments().stream()
              .filter(item -> Objects.isNull(item.getId()) || idsToKeep.contains(item.getId()))
              .map(item -> saveIdentityDocument(model.getId(), item))
              .collect(Collectors.toList())
      );
    }
    return model;
  }

  private Person buildPhones(final Person model) {
    if (Objects.nonNull(model.getPhones())) {
      final List<Integer> idsToKeep = keepOnlyPhoneExists(model);
      return model.withPhones(
          model.getPhones().stream()
              .filter(item -> Objects.isNull(item.getId()) || idsToKeep.contains(item.getId()))
              .map(item -> savePhone(model.getId(), item))
              .collect(Collectors.toList())
      );
    }
    return model;
  }

  private Person buildEmails(final Person model) {
    if (Objects.nonNull(model.getEmails())) {
      final List<Integer> idsToKeep = keepOnlyEmailsExists(model);
      return model.withEmails(
          model.getEmails().stream()
              .filter(item -> Objects.isNull(item.getId()) || idsToKeep.contains(item.getId()))
              .map(item -> saveEmail(model.getId(), item))
              .collect(Collectors.toList())
      );
    }
    return model;
  }

  private Person buildAddresses(final Person model) {
    if (Objects.nonNull(model.getAddresses())) {
      final List<Integer> idsToKeep = keepOnlyAddressesExists(model);
      return model.withAddresses(
          model.getAddresses().stream()
              .filter(item -> Objects.isNull(item.getId()) || idsToKeep.contains(item.getId()))
              .map(item -> saveAddress(model.getId(), item))
              .collect(Collectors.toList())
      );
    }
    return model;
  }

  /*
   * IdentityDocument actions
   */

  public IdentityDocument saveIdentityDocument(final Integer personId, final IdentityDocument model) {
    validIdentityDocumentType(model.getType());
    validCountry(model.getCountry());

    return identityDocumentService.save(buildIdentityDocument(model), personId);
  }

  public void deleteIdentityDocumentById(final Integer personId, final Integer id) {
    if (Objects.nonNull(identityDocumentService.findByIdAndPersonId(personId, id))) {
      identityDocumentService.deleteByIdAndPersonId(personId, id);
    }
  }

  /*
   * Phone actions
   */

  public Phone savePhone(final Integer personId, final Phone model) {
    validAttributeType(model.getAttributeType());
    return phoneService.save(buildPhone(model), personId);
  }

  public void validatePhone(final Integer personId, final Integer id, final Boolean valid) {
    final Phone model = phoneService.findByIdAndPersonId(personId, id);
    phoneService.save(model.withValid(valid).withCheckTime(LocalDateTime.now()), personId);
  }

  public void deletePhoneById(final Integer personId, final Integer id) {
    if (Objects.nonNull(phoneService.findByIdAndPersonId(personId, id))) {
      phoneService.deleteByIdAndPersonId(personId, id);
    }
  }

  /*
   * Emails actions
   */

  public Email saveEmail(final Integer personId, final Email model) {
    validAttributeType(model.getAttributeType());
    return emailService.save(buildEmail(model), personId);
  }

  public void validateEmail(final Integer personId, final Integer id, final Boolean valid) {
    final Email model = emailService.findByIdAndPersonId(personId, id);
    emailService.save(model.withValid(valid).withCheckTime(LocalDateTime.now()), personId);
  }

  public void deleteEmailById(final Integer personId, final Integer id) {
    if (Objects.nonNull(emailService.findByIdAndPersonId(personId, id))) {
      emailService.deleteByIdAndPersonId(personId, id);
    }
  }

  /*
   * Addresses actions
   */

  public Address saveAddress(final Integer personId, final Address model) {
    validAttributeType(model.getAttributeType());
    validCountriesInAddresses(List.of(model));
    return addressService.save(buildAddress(model), personId);
  }

  private void validAttributeType(final AttributeType model) {
    if (Objects.nonNull(model)) {
      attributeTypeService.existsById(model.getId());
    }
  }

  public void deleteAddressById(final Integer personId, final Integer id) {
    if (Objects.nonNull(addressService.findByIdAndPersonId(personId, id))) {
      addressService.deleteByIdAndPersonId(personId, id);
    }
  }

  /*
   * Build methods
   */
  private IdentityDocument buildIdentityDocument(final IdentityDocument item) {
    final IdentityDocument built = buildIdentityDocumentFromDataBase(item);
    return built
        .withCreationTime(
            Objects.isNull(built.getCreationTime())
                ? LocalDateTime.now()
                : built.getCreationTime()
        );
  }

  private IdentityDocument buildIdentityDocumentFromDataBase(final IdentityDocument item) {
    if (Objects.nonNull(item.getId())) {
      final IdentityDocument old = identityDocumentService.findById(item.getId());
      return old
          .withValue(item.getValue())
          .withCountry(item.getCountry())
          .withType(item.getType())
          .withModificationTime(LocalDateTime.now());
    }
    return item;
  }

  private Phone buildPhone(final Phone item) {
    final Phone built = buildPhoneFromDataBase(item);
    return built
        .withCreationTime(
            Objects.isNull(built.getCreationTime())
                ? LocalDateTime.now()
                : built.getCreationTime()
        )
        .withCheckTime(
            Objects.nonNull(built.getValid()) && Objects.isNull(built.getCheckTime())
                ? LocalDateTime.now()
                : built.getCheckTime()
        );
  }

  private Phone buildPhoneFromDataBase(final Phone item) {
    if (Objects.nonNull(item.getId())) {
      final Phone old = phoneService.findById(item.getId());
      return old
          .withCode(item.getCode())
          .withValue(item.getValue())
          .withValid(item.getValid())
          .withAttributeType(item.getAttributeType())
          .withCheckTime(
              Objects.nonNull(old.getValid()) && Objects.nonNull(item.getValid())
                  && item.getValid().booleanValue() != old.getValid().booleanValue()
                  ? LocalDateTime.now()
                  : Objects.isNull(item.getValid()) ? null : old.getCheckTime()
          )
          .withModificationTime(LocalDateTime.now());
    }
    return item;
  }

  private Email buildEmail(final Email item) {
    final Email built = buildEmailFromDataBase(item);
    return built
        .withCreationTime(
            Objects.isNull(built.getCreationTime())
                ? LocalDateTime.now()
                : built.getCreationTime()
        )
        .withCheckTime(
            Objects.nonNull(built.getValid()) && Objects.isNull(built.getCheckTime())
                ? LocalDateTime.now()
                : built.getCheckTime()
        );
  }

  private Email buildEmailFromDataBase(final Email item) {
    if (Objects.nonNull(item.getId())) {
      final Email old = emailService.findById(item.getId());
      return old
          .withValue(item.getValue())
          .withValid(item.getValid())
          .withAttributeType(item.getAttributeType())
          .withCheckTime(
              Objects.nonNull(old.getValid()) && Objects.nonNull(item.getValid())
                  && item.getValid().booleanValue() != old.getValid().booleanValue()
                  ? LocalDateTime.now()
                  : Objects.isNull(item.getValid()) ? null : old.getCheckTime()
          )
          .withModificationTime(LocalDateTime.now());
    }
    return item;
  }

  private Address buildAddress(final Address item) {
    final Address built = buildAddressFromDataBase(item);
    return built
        .withCreationTime(
            Objects.isNull(built.getCreationTime())
                ? LocalDateTime.now()
                : built.getCreationTime()
        );
  }

  private Address buildAddressFromDataBase(final Address item) {
    if (Objects.nonNull(item.getId())) {
      final Address old = addressService.findById(item.getId());
      return old
          .withCountry(item.getCountry())
          .withProvince(item.getProvince())
          .withStreet(item.getStreet())
          .withComplement(item.getComplement())
          .withZipCode(item.getZipCode())
          .withCity(item.getCity())
          .withAttributeType(item.getAttributeType())
          .withModificationTime(LocalDateTime.now());
    }
    return item;
  }

  /*
   * Remove orphan elements
   */

  private List<Integer> keepOnlyIdentityDocumentsExists(final Person model) {
    if (Objects.nonNull(model.getId())) {
      final List<Integer> ids = model.getIdentityDocuments().stream()
          .map(IdentityDocument::getId)
          .filter(Objects::nonNull)
          .collect(Collectors.toList());
      identityDocumentService.keepOnlyExists(ids, model.getId());
      return ids;
    }
    return Collections.emptyList();
  }

  private List<Integer> keepOnlyPhoneExists(final Person model) {
    if (Objects.nonNull(model.getId())) {
      final List<Integer> ids = model.getPhones().stream()
          .map(Phone::getId)
          .filter(Objects::nonNull)
          .collect(Collectors.toList());
      phoneService.keepOnlyExists(ids, model.getId());
      return ids;
    }
    return Collections.emptyList();
  }

  private List<Integer> keepOnlyEmailsExists(final Person model) {
    if (Objects.nonNull(model.getId())) {
      final List<Integer> ids = model.getEmails().stream()
          .map(Email::getId)
          .filter(Objects::nonNull)
          .collect(Collectors.toList());
      emailService.keepOnlyExists(ids, model.getId());
      return ids;
    }
    return Collections.emptyList();
  }

  private List<Integer> keepOnlyAddressesExists(final Person model) {
    if (Objects.nonNull(model.getId())) {
      final List<Integer> ids = model.getAddresses().stream()
          .map(Address::getId)
          .filter(Objects::nonNull)
          .collect(Collectors.toList());
      addressService.keepOnlyExists(ids, model.getId());
      return ids;
    }
    return Collections.emptyList();
  }
}
