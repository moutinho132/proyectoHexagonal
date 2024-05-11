package com.martzatech.vdhg.crmprojectback.domains.customers.repositories;

import com.martzatech.vdhg.crmprojectback.Application;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.CompanyEntity;
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
class CompanyRepositoryITests {

  private static final AtomicInteger ID_WRAPPER = new AtomicInteger();
  private static final String NAME = "NAME";
  private static final String OTHER_NAME = "OTHER_NAME";

  @Autowired
  private CompanyRepository tested;

  @Order(1)
  @Test
  void givenANewObject_whenSave_thenNewObjectIsPersisted() {
    final CompanyEntity entity = CompanyEntity.builder().name(NAME).build();

    final CompanyEntity actual = tested.save(entity);

    Assertions.assertNotNull(actual);
    Assertions.assertNotNull(actual.getId());
    Assertions.assertEquals(NAME, actual.getName());

    ID_WRAPPER.set(actual.getId());
  }

  @Order(2)
  @Test
  void whenFindAll_thenReturnAnOnlyObject() {
    Assertions.assertTrue(CollectionUtils.isEmpty(tested.findAll()));
  }

  @Order(3)
  @Test
  void whenFindById_thenReturnPersistedObject() {
    final Optional<CompanyEntity> actual = tested.findById(ID_WRAPPER.get());

    Assertions.assertFalse(actual.isPresent());
  }

  @Order(4)
  @Test
  void givenAExistingObject_whenSave_thenUpdateObject() {
    final CompanyEntity existing = CompanyEntity.builder().id(ID_WRAPPER.get()).name(OTHER_NAME).build();

    final CompanyEntity actual = tested.save(existing);

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
