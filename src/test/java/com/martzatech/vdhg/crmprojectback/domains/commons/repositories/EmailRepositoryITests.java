package com.martzatech.vdhg.crmprojectback.domains.commons.repositories;

import com.martzatech.vdhg.crmprojectback.Application;
import com.martzatech.vdhg.crmprojectback.domains.commons.entities.AttributeTypeEntity;
import com.martzatech.vdhg.crmprojectback.domains.commons.entities.EmailEntity;
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
class EmailRepositoryITests {

  private static final AtomicInteger ID_WRAPPER = new AtomicInteger();
  private static final String VALUE = "VALUE";
  private static final String OTHER_VALUE = "OTHER_VALUE";
  private static final Boolean VALID = Boolean.FALSE;
  private static final Boolean OTHER_VALID = Boolean.TRUE;
  private static final LocalDateTime CHECK_TIME = LocalDateTime.now();
  private static final LocalDateTime OTHER_CHECK_TIME = LocalDateTime.now().plusHours(1);

  @Autowired
  private EmailRepository tested;

  @Autowired
  private AttributeTypeRepository attributeTypeRepository;

  @Order(1)
  @Test
  void givenANewObject_whenSave_thenNewObjectIsPersisted() {
    final AttributeTypeEntity attributeTypeEntity = attributeTypeRepository.save(AttributeTypeEntity.builder().build());
    final EmailEntity entity = EmailEntity.builder().value(VALUE).valid(VALID).checkTime(CHECK_TIME)
        .attributeType(attributeTypeEntity).build();

    final EmailEntity actual = tested.save(entity);

    Assertions.assertNotNull(actual);
    Assertions.assertNotNull(actual.getId());
    Assertions.assertEquals(VALUE, actual.getValue());
    Assertions.assertEquals(VALID, actual.getValid());
    Assertions.assertEquals(CHECK_TIME, actual.getCheckTime());
    Assertions.assertNotNull(actual.getAttributeType());
    Assertions.assertEquals(attributeTypeEntity.getId(), actual.getAttributeType().getId());

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
    final Optional<EmailEntity> actual = tested.findById(ID_WRAPPER.get());

    Assertions.assertTrue(actual.isPresent());
  }

  @Order(4)
  @Test
  void givenAExistingObject_whenSave_thenUpdateObject() {
    final AttributeTypeEntity otherAttributeTypeEntity = attributeTypeRepository.save(
        AttributeTypeEntity.builder().build());
    final EmailEntity existing = EmailEntity.builder().id(ID_WRAPPER.get()).value(OTHER_VALUE).valid(OTHER_VALID)
        .checkTime(OTHER_CHECK_TIME).attributeType(otherAttributeTypeEntity).build();

    final EmailEntity actual = tested.save(existing);

    Assertions.assertEquals(ID_WRAPPER.get(), actual.getId());
    Assertions.assertEquals(OTHER_VALUE, actual.getValue());
    Assertions.assertEquals(OTHER_VALID, actual.getValid());
    Assertions.assertEquals(OTHER_CHECK_TIME, actual.getCheckTime());
    Assertions.assertNotNull(actual.getAttributeType());
    Assertions.assertEquals(otherAttributeTypeEntity.getId(), actual.getAttributeType().getId());
  }

  @Order(5)
  @Test
  void whenDeleteById_thenDeleteObject() {
    tested.deleteById(ID_WRAPPER.get());

    Assertions.assertTrue(tested.findById(ID_WRAPPER.get()).isEmpty());
  }
}
