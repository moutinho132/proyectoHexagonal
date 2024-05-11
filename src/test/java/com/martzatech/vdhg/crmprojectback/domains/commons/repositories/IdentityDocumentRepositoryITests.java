package com.martzatech.vdhg.crmprojectback.domains.commons.repositories;

import com.martzatech.vdhg.crmprojectback.Application;
import com.martzatech.vdhg.crmprojectback.domains.commons.entities.CountryEntity;
import com.martzatech.vdhg.crmprojectback.domains.commons.entities.IdentityDocumentEntity;
import com.martzatech.vdhg.crmprojectback.domains.commons.entities.IdentityDocumentTypeEntity;
import com.martzatech.vdhg.crmprojectback.domains.commons.entities.PersonEntity;

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
class IdentityDocumentRepositoryITests {

  private static final AtomicInteger ID_WRAPPER = new AtomicInteger();
  private static final String VALUE = "VALUE";
  private static final String OTHER_VALUE = "OTHER_VALUE";

  @Autowired
  private IdentityDocumentRepository tested;

  @Autowired
  private IdentityDocumentTypeRepository identityDocumentTypeRepository;

  @Autowired
  private CountryRepository countryRepository;

  @Autowired
  private PersonRepository personRepository;

  @Order(1)
  @Test
  void givenANewObject_whenSave_thenNewObjectIsPersisted() {
    final IdentityDocumentTypeEntity identityDocumentTypeEntity = identityDocumentTypeRepository
        .save(IdentityDocumentTypeEntity.builder().build());
    final CountryEntity countryEntity = countryRepository
        .save(CountryEntity.builder().build());
    final PersonEntity personEntity = personRepository
        .save(PersonEntity.builder().build());
    final IdentityDocumentEntity entity = IdentityDocumentEntity.builder().value(VALUE)
        .type(identityDocumentTypeEntity).country(countryEntity).person(personEntity).build();

    final IdentityDocumentEntity actual = tested.save(entity);

    Assertions.assertNotNull(actual);
    Assertions.assertNotNull(actual.getId());
    Assertions.assertEquals(VALUE, actual.getValue());
    Assertions.assertNotNull(actual.getType());
    Assertions.assertEquals(identityDocumentTypeEntity.getId(), actual.getType().getId());
    Assertions.assertNotNull(actual.getCountry());
    Assertions.assertEquals(countryEntity.getId(), actual.getCountry().getId());
    Assertions.assertNotNull(actual.getPerson());
    Assertions.assertEquals(personEntity.getId(), actual.getPerson().getId());

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
    final Optional<IdentityDocumentEntity> actual = tested.findById(ID_WRAPPER.get());

    Assertions.assertTrue(actual.isPresent());
  }

  @Order(4)
  @Test
  void givenAExistingObject_whenSave_thenUpdateObject() {
    final IdentityDocumentTypeEntity otherIdentityDocumentTypeEntity = identityDocumentTypeRepository
        .save(IdentityDocumentTypeEntity.builder().build());
    final CountryEntity otherCountryEntity = countryRepository
        .save(CountryEntity.builder().build());
    final PersonEntity otherPersonEntity = personRepository
        .save(PersonEntity.builder().build());
    final IdentityDocumentEntity existing = IdentityDocumentEntity.builder().id(ID_WRAPPER.get()).value(OTHER_VALUE)
        .type(otherIdentityDocumentTypeEntity).country(otherCountryEntity).person(otherPersonEntity)
        .build();

    final IdentityDocumentEntity actual = tested.save(existing);

    Assertions.assertEquals(ID_WRAPPER.get(), actual.getId());
    Assertions.assertEquals(OTHER_VALUE, actual.getValue());
    Assertions.assertNotNull(actual.getType());
    Assertions.assertEquals(otherIdentityDocumentTypeEntity.getId(), actual.getType().getId());
    Assertions.assertNotNull(actual.getCountry());
    Assertions.assertEquals(otherCountryEntity.getId(), actual.getCountry().getId());
    Assertions.assertNotNull(actual.getPerson());
    Assertions.assertEquals(otherPersonEntity.getId(), actual.getPerson().getId());
  }
}
