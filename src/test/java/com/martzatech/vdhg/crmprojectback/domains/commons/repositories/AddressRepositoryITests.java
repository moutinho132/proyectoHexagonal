package com.martzatech.vdhg.crmprojectback.domains.commons.repositories;

import com.martzatech.vdhg.crmprojectback.Application;
import com.martzatech.vdhg.crmprojectback.domains.commons.entities.AddressEntity;
import com.martzatech.vdhg.crmprojectback.domains.commons.entities.AttributeTypeEntity;
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
class AddressRepositoryITests {

  private static final AtomicInteger ID_WRAPPER = new AtomicInteger();
  private static final String PROVINCE = "NAME";
  private static final String OTHER_PROVINCE = "OTHER_PROVINCE";
  private static final String STREET = "STREET";
  private static final String OTHER_STREET = "OTHER_STREET";
  private static final String COMPLEMENT = "COMPLEMENT";
  private static final String OTHER_COMPLEMENT = "OTHER_COMPLEMENT";
  private static final String ZIP_CODE = "ZIP_CODE";
  private static final String OTHER_ZIP_CODE = "OTHER_ZIP_CODE";
  private static final String CITY = "CITY";
  private static final String OTHER_CITY = "OTHER_CITY";

  @Autowired
  private AddressRepository tested;

  @Autowired
  private AttributeTypeRepository attributeTypeRepository;

  @Order(1)
  @Test
  void givenANewObject_whenSave_thenNewObjectIsPersisted() {
    final AttributeTypeEntity attributeTypeEntity = attributeTypeRepository.save(AttributeTypeEntity.builder().build());
    final AddressEntity entity = AddressEntity.builder().province(PROVINCE).street(STREET).complement(COMPLEMENT)
        .zipCode(ZIP_CODE).city(CITY).attributeType(attributeTypeEntity).build();

    final AddressEntity actual = tested.save(entity);

    Assertions.assertNotNull(actual);
    Assertions.assertNotNull(actual.getId());
    Assertions.assertEquals(PROVINCE, actual.getProvince());
    Assertions.assertEquals(STREET, actual.getStreet());
    Assertions.assertEquals(COMPLEMENT, actual.getComplement());
    Assertions.assertEquals(ZIP_CODE, actual.getZipCode());
    Assertions.assertEquals(CITY, actual.getCity());
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
    final Optional<AddressEntity> actual = tested.findById(ID_WRAPPER.get());

    Assertions.assertTrue(actual.isPresent());
  }

  @Order(4)
  @Test
  void givenAExistingObject_whenSave_thenUpdateObject() {
    final AttributeTypeEntity otherAttributeTypeEntity = attributeTypeRepository.save(
        AttributeTypeEntity.builder().build());
    final AddressEntity existing = AddressEntity.builder().id(ID_WRAPPER.get()).province(OTHER_PROVINCE)
        .street(OTHER_STREET)
        .complement(OTHER_COMPLEMENT).zipCode(OTHER_ZIP_CODE).city(OTHER_CITY).attributeType(otherAttributeTypeEntity)
        .build();

    final AddressEntity actual = tested.save(existing);

    Assertions.assertEquals(ID_WRAPPER.get(), actual.getId());
    Assertions.assertEquals(OTHER_PROVINCE, actual.getProvince());
    Assertions.assertEquals(OTHER_STREET, actual.getStreet());
    Assertions.assertEquals(OTHER_COMPLEMENT, actual.getComplement());
    Assertions.assertEquals(OTHER_ZIP_CODE, actual.getZipCode());
    Assertions.assertEquals(OTHER_CITY, actual.getCity());
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
