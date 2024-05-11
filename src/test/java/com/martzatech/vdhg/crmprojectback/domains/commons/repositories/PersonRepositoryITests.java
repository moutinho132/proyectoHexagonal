package com.martzatech.vdhg.crmprojectback.domains.commons.repositories;

import com.martzatech.vdhg.crmprojectback.Application;
import com.martzatech.vdhg.crmprojectback.domains.commons.entities.AddressEntity;
import com.martzatech.vdhg.crmprojectback.domains.commons.entities.CountryEntity;
import com.martzatech.vdhg.crmprojectback.domains.commons.entities.EmailEntity;
import com.martzatech.vdhg.crmprojectback.domains.commons.entities.GenderEntity;
import com.martzatech.vdhg.crmprojectback.domains.commons.entities.IdentityDocumentEntity;
import com.martzatech.vdhg.crmprojectback.domains.commons.entities.PersonEntity;
import com.martzatech.vdhg.crmprojectback.domains.commons.entities.PhoneEntity;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.CollectionUtils;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@TestMethodOrder(OrderAnnotation.class)
class PersonRepositoryITests {

  private static final AtomicInteger ID_WRAPPER = new AtomicInteger();
  private static final String NAME = "NAME";
  private static final String OTHER_NAME = "OTHER_NAME";
  private static final String SURNAME = "SURNAME";
  private static final String OTHER_SURNAME = "OTHER_SURNAME";

  @Autowired
  private PersonRepository tested;

  @Autowired
  private IdentityDocumentRepository identityDocumentRepository;

  @Autowired
  private PhoneRepository phoneRepository;

  @Autowired
  private EmailRepository emailRepository;

  @Autowired
  private AddressRepository addressRepository;

  @Autowired
  private CountryRepository countryRepository;

  @Autowired
  private GenderRepository genderRepository;

  @Order(1)
  @Test
  void givenANewObject_whenSave_thenNewObjectIsPersisted() {
    final CountryEntity countryEntity = countryRepository.save(CountryEntity.builder().build());
    final GenderEntity genderEntity = genderRepository.save(GenderEntity.builder().build());
    final PersonEntity entity = PersonEntity.builder().name(NAME).surname(SURNAME)
        .nationality(countryEntity).gender(genderEntity).build();

    final PersonEntity actual = tested.save(entity);

    Assertions.assertNotNull(actual);
    Assertions.assertNotNull(actual.getId());
    Assertions.assertEquals(NAME, actual.getName());
    Assertions.assertEquals(SURNAME, actual.getSurname());
    Assertions.assertNotNull(actual.getNationality());
    Assertions.assertEquals(countryEntity.getId(), actual.getNationality().getId());
    Assertions.assertNotNull(actual.getGender());
    Assertions.assertEquals(genderEntity.getId(), actual.getGender().getId());

    ID_WRAPPER.set(actual.getId());
  }

  @Order(2)
  @Test
  void whenFindAll_thenReturnAnOnlyObject() {
    Assertions.assertFalse(CollectionUtils.isEmpty(tested.findAll()));
  }

  @Order(3)
  @Test
  void whenFindById_thenReturnPersistedObject() {
    final Optional<PersonEntity> actual = tested.findById(ID_WRAPPER.get());

    Assertions.assertTrue(actual.isPresent());
  }

  @Order(4)
  @Test
  void givenAExistingObject_whenSave_thenUpdateObject() {
    final CountryEntity otherCountryEntity = countryRepository.save(CountryEntity.builder().build());
    final GenderEntity otherGenderEntity = genderRepository.save(GenderEntity.builder().build());
    final PersonEntity existing = PersonEntity.builder().id(ID_WRAPPER.get()).name(OTHER_NAME).surname(OTHER_SURNAME)
        .nationality(otherCountryEntity).gender(otherGenderEntity).addresses(Collections.emptyList())
        .phones(Collections.emptyList()).emails(Collections.emptyList()).identityDocuments(Collections.emptyList())
        .build();

    final PersonEntity actual = tested.save(existing);

    Assertions.assertNotNull(actual);
    Assertions.assertEquals(ID_WRAPPER.get(), actual.getId());
    Assertions.assertEquals(OTHER_NAME, actual.getName());
    Assertions.assertEquals(OTHER_SURNAME, actual.getSurname());
    Assertions.assertNotNull(actual.getNationality());
    Assertions.assertEquals(otherCountryEntity.getId(), actual.getNationality().getId());
    Assertions.assertNotNull(actual.getGender());
    Assertions.assertEquals(otherGenderEntity.getId(), actual.getGender().getId());
  }

  @Order(5)
  @Test
  void givenManyToOneRelationShips_whenFindById_thenRetrieveEachList() {
    final IdentityDocumentEntity identityDocumentEntity = identityDocumentRepository
        .save(IdentityDocumentEntity.builder().person(PersonEntity.builder().id(ID_WRAPPER.get()).build()).build());
    final PhoneEntity phoneEntity = phoneRepository.save(PhoneEntity.builder().person(PersonEntity.builder()
        .id(ID_WRAPPER.get()).build()).build());
    final EmailEntity emailEntity = emailRepository.save(EmailEntity.builder()
        .person(PersonEntity.builder().id(ID_WRAPPER.get()).build()).build());
    final AddressEntity addressEntity = addressRepository.save(AddressEntity.builder()
        .person(PersonEntity.builder().id(ID_WRAPPER.get()).build()).build());

    final Optional<PersonEntity> actual = tested.findById(ID_WRAPPER.get());

    Assertions.assertTrue(actual.isPresent());
    Assertions.assertFalse(CollectionUtils.isEmpty(actual.get().getIdentityDocuments()));
    Assertions.assertEquals(1, actual.get().getIdentityDocuments().size());
    Assertions.assertTrue(actual.get().getIdentityDocuments().stream()
        .map(IdentityDocumentEntity::getId).anyMatch(id -> id.intValue() == identityDocumentEntity.getId()));
    Assertions.assertFalse(CollectionUtils.isEmpty(actual.get().getPhones()));
    Assertions.assertEquals(1, actual.get().getPhones().size());
    Assertions.assertTrue(actual.get().getPhones().stream()
        .map(PhoneEntity::getId).anyMatch(id -> id.intValue() == phoneEntity.getId()));
    Assertions.assertFalse(CollectionUtils.isEmpty(actual.get().getEmails()));
    Assertions.assertEquals(1, actual.get().getEmails().size());
    Assertions.assertTrue(actual.get().getEmails().stream()
        .map(EmailEntity::getId).anyMatch(id -> id.intValue() == emailEntity.getId()));
    Assertions.assertFalse(CollectionUtils.isEmpty(actual.get().getAddresses()));
    Assertions.assertEquals(1, actual.get().getAddresses().size());
    Assertions.assertTrue(actual.get().getAddresses().stream()
        .map(AddressEntity::getId).anyMatch(id -> id.intValue() == addressEntity.getId()));
  }

  @Order(6)
  @Test
  void whenDeleteById_thenDeleteObject() {
    tested.deleteById(ID_WRAPPER.get());

    Assertions.assertTrue(tested.findById(ID_WRAPPER.get()).isEmpty());
  }
}
