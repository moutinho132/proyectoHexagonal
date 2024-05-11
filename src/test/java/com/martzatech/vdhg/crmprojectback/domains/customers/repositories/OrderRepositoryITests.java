package com.martzatech.vdhg.crmprojectback.domains.customers.repositories;

import com.martzatech.vdhg.crmprojectback.Application;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.OfferEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.OrderEntity;
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

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@TestMethodOrder(OrderAnnotation.class)
class OrderRepositoryITests {

  private static final AtomicInteger ID_WRAPPER = new AtomicInteger();
  private static final LocalDateTime CREATION_TIME = LocalDateTime.now();
  private static final LocalDateTime OTHER_CREATION_TIME = LocalDateTime.now().plusHours(1);
  private static final String NAME = "NAME";

  @Autowired
  private OrderRepository tested;

  @Autowired
  private OfferRepository offerRepository;

  @Order(1)
  @Test
  void givenANewObject_whenSave_thenNewObjectIsPersisted() {
    final OfferEntity offer = offerRepository.save(OfferEntity.builder().name(NAME).build());
    final OrderEntity entity = OrderEntity.builder()
        .creationTime(CREATION_TIME).offer(offer).build();

    final OrderEntity actual = tested.save(entity);

    Assertions.assertNotNull(actual);
    Assertions.assertNotNull(actual.getId());
    Assertions.assertEquals(CREATION_TIME, actual.getCreationTime());
    Assertions.assertEquals(offer.getId(), actual.getOffer().getId());

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
    final Optional<OrderEntity> actual = tested.findById(ID_WRAPPER.get());

    Assertions.assertFalse(actual.isPresent());
  }

 /* @Order(4)
  @Test
  void givenAExistingObject_whenSave_thenUpdateObject() {
    final OfferEntity otherOffer = offerRepository.save(OfferEntity.builder().name(NAME).build());
    final OrderEntity existing = OrderEntity.builder().id(ID_WRAPPER.get())
        .creationTime(OTHER_CREATION_TIME).offer(otherOffer).build();

    final OrderEntity actual = tested.save(existing);

    Assertions.assertEquals(ID_WRAPPER.get(), actual.getId());
    Assertions.assertEquals(OTHER_CREATION_TIME, actual.getCreationTime());
    Assertions.assertEquals(otherOffer.getId(), actual.getOffer().getId());
  }*/

  /*@Order(5)
  @Test
  void whenDeleteById_thenDeleteObject() {
    tested.deleteById(ID_WRAPPER.get());

    Assertions.assertTrue(tested.findById(ID_WRAPPER.get()).isEmpty());
  }*/
}
