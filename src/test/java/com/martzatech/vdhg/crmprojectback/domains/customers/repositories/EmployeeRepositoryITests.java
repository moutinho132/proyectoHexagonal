package com.martzatech.vdhg.crmprojectback.domains.customers.repositories;

import com.martzatech.vdhg.crmprojectback.Application;
import com.martzatech.vdhg.crmprojectback.domains.commons.entities.PersonEntity;
import com.martzatech.vdhg.crmprojectback.domains.commons.repositories.PersonRepository;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.EmployeeEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.EmployeeTypeEntity;
import java.time.LocalDateTime;
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
class EmployeeRepositoryITests {

  private static final AtomicInteger ID_WRAPPER = new AtomicInteger();
  private static final LocalDateTime CREATION_TIME = LocalDateTime.now();
  private static final LocalDateTime OTHER_CREATION_TIME = LocalDateTime.now().plusHours(1);

  @Autowired
  private EmployeeRepository tested;

  @Autowired
  private EmployeeTypeRepository employeeTypeRepository;

  @Autowired
  private PersonRepository personRepository;

  @Order(1)
  @Test
  void givenANewObject_whenSave_thenNewObjectIsPersisted() {
    final EmployeeTypeEntity employeeTypeEntity = employeeTypeRepository.save(EmployeeTypeEntity.builder().build());
    final PersonEntity personEntity = personRepository.save(PersonEntity.builder().build());
    final EmployeeEntity entity = EmployeeEntity.builder().creationTime(CREATION_TIME).type(employeeTypeEntity)
        .person(personEntity).build();

    final EmployeeEntity actual = tested.save(entity);

    Assertions.assertNotNull(actual);
    Assertions.assertNotNull(actual.getId());
    Assertions.assertEquals(CREATION_TIME, actual.getCreationTime());
    Assertions.assertNotNull(actual.getType());
    Assertions.assertEquals(employeeTypeEntity.getId(), actual.getType().getId());
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
    final Optional<EmployeeEntity> actual = tested.findById(ID_WRAPPER.get());

    Assertions.assertTrue(actual.isPresent());
  }

  @Order(4)
  @Test
  void givenAExistingObject_whenSave_thenUpdateObject() {
    final EmployeeTypeEntity otherEmployeeTypeEntity = employeeTypeRepository
        .save(EmployeeTypeEntity.builder().build());
    final PersonEntity otherPersonEntity = personRepository.save(PersonEntity.builder().build());
    final EmployeeEntity existing = EmployeeEntity.builder().id(ID_WRAPPER.get()).creationTime(OTHER_CREATION_TIME)
        .type(otherEmployeeTypeEntity).person(otherPersonEntity)
        .build();

    final EmployeeEntity actual = tested.save(existing);

    Assertions.assertEquals(ID_WRAPPER.get(), actual.getId());
    Assertions.assertEquals(OTHER_CREATION_TIME, actual.getCreationTime());
    Assertions.assertNotNull(actual.getType());
    Assertions.assertEquals(otherEmployeeTypeEntity.getId(), actual.getType().getId());
    Assertions.assertNotNull(actual.getPerson());
    Assertions.assertEquals(otherPersonEntity.getId(), actual.getPerson().getId());
  }

  @Order(5)
  @Test
  void whenDeleteById_thenDeleteObject() {
    tested.deleteById(ID_WRAPPER.get());

    Assertions.assertTrue(tested.findById(ID_WRAPPER.get()).isEmpty());
  }
}
