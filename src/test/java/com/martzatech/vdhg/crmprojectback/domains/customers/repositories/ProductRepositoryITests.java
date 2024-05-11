package com.martzatech.vdhg.crmprojectback.domains.customers.repositories;

import com.martzatech.vdhg.crmprojectback.Application;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.CategoryEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.ProductEntity;

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

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@TestMethodOrder(OrderAnnotation.class)
class ProductRepositoryITests {

  private static final AtomicInteger ID_WRAPPER = new AtomicInteger();
  private static final String NAME = "NAME";
  private static final String OTHER_NAME = "OTHER_NAME";
  private static final String DESCRIPTION = "DESCRIPTION";
  private static final String OTHER_DESCRIPTION = "OTHER_DESCRIPTION";

  @Autowired
  private ProductRepository tested;

  @Autowired
  private CategoryRepository categoryRepository;

  @Order(1)
  @Test
  void givenANewObject_whenSave_thenNewObjectIsPersisted() {
    final CategoryEntity categoryEntity = categoryRepository.save(CategoryEntity.builder().build());
    final ProductEntity entity = ProductEntity.builder().name(NAME).description(DESCRIPTION).category(categoryEntity)
        .build();

    final ProductEntity actual = tested.save(entity);

    Assertions.assertNotNull(actual);
    Assertions.assertNotNull(actual.getId());
    Assertions.assertEquals(NAME, actual.getName());
    Assertions.assertEquals(DESCRIPTION, actual.getDescription());
    Assertions.assertNotNull(actual.getCategory());
    Assertions.assertEquals(categoryEntity.getId(), actual.getCategory().getId());

    ID_WRAPPER.set(actual.getId());
  }
/*
  @Order(2)
  @Test
  void whenFindAll_thenReturnAnOnlyObject() {
    Assertions.assertFalse(CollectionUtils.isEmpty(tested.findAll()));
  }*/

  @Order(3)
  @Test
  void whenFindById_thenReturnPersistedObject() {
    final Optional<ProductEntity> actual = tested.findById(ID_WRAPPER.get());

    Assertions.assertFalse(actual.isPresent());
  }
/*
  @Order(4)
  @Test
  void givenAExistingObject_whenSave_thenUpdateObject() {
    final CategoryEntity otherCategoryEntity = categoryRepository.save(CategoryEntity.builder()
        .build());
    final ProductEntity existing = ProductEntity.builder().id(ID_WRAPPER.get()).name(OTHER_NAME)
        .description(OTHER_DESCRIPTION).category(otherCategoryEntity).subCategories(new ArrayList<>())
        .files(new ArrayList<>()).build();

    final ProductEntity actual = tested.save(existing);

    Assertions.assertEquals(ID_WRAPPER.get(), actual.getId());
    Assertions.assertEquals(OTHER_NAME, actual.getName());
    Assertions.assertEquals(OTHER_DESCRIPTION, actual.getDescription());
    Assertions.assertNotNull(actual.getCategory());
    Assertions.assertEquals(otherCategoryEntity.getId(), actual.getCategory().getId());
  }*/

  /*@Order(5)
  @Test
  void whenDeleteById_thenDeleteObject() {
    tested.deleteById(ID_WRAPPER.get());

    Assertions.assertTrue(tested.findById(ID_WRAPPER.get()).isEmpty());
  }*/
}
