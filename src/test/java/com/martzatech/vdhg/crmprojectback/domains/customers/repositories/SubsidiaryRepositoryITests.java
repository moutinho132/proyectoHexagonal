package com.martzatech.vdhg.crmprojectback.domains.customers.repositories;

import com.martzatech.vdhg.crmprojectback.Application;
import com.martzatech.vdhg.crmprojectback.domains.security.entities.SubsidiaryEntity;
import com.martzatech.vdhg.crmprojectback.domains.security.repositories.SubsidiaryRepository;
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
class SubsidiaryRepositoryITests {

  private static final AtomicInteger ID_WRAPPER = new AtomicInteger();
  private static final String NAME = "NAME";
  private static final String OTHER_NAME = "OTHER_NAME";

  @Autowired
  private SubsidiaryRepository tested;

  @Order(1)
  @Test
  void givenANewObject_whenSave_thenNewObjectIsPersisted() {
    final SubsidiaryEntity entity = SubsidiaryEntity.builder().name(NAME).build();

    final SubsidiaryEntity actual = tested.save(entity);

    Assertions.assertNotNull(actual);
    Assertions.assertNotNull(actual.getId());
    Assertions.assertEquals(NAME, actual.getName());

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
    final Optional<SubsidiaryEntity> actual = tested.findById(ID_WRAPPER.get());

    Assertions.assertTrue(actual.isPresent());
  }

  @Order(4)
  @Test
  void givenAExistingObject_whenSave_thenUpdateObject() {
    final SubsidiaryEntity existing = SubsidiaryEntity.builder().id(ID_WRAPPER.get()).name(OTHER_NAME).build();

    final SubsidiaryEntity actual = tested.save(existing);

    Assertions.assertEquals(ID_WRAPPER.get(), actual.getId());
    Assertions.assertEquals(OTHER_NAME, actual.getName());
  }

  @Order(5)
  @Test
  void whenDeleteById_thenDeleteObject() {
    tested.deleteById(ID_WRAPPER.get());

    Assertions.assertTrue(tested.findById(ID_WRAPPER.get()).isEmpty());
  }
}
