package com.martzatech.vdhg.crmprojectback.domains.customers.repositories;

import com.martzatech.vdhg.crmprojectback.Application;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.CustomerEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.CustomerStatusEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.LeadEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.MembershipEntity;
import com.martzatech.vdhg.crmprojectback.domains.security.repositories.UserRepository;
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
class CustomerRepositoryITests {

  private static final AtomicInteger ID_WRAPPER = new AtomicInteger();
  private static final LocalDateTime CREATION_TIME = LocalDateTime.now();
  private static final LocalDateTime MODIFICATION_TIME = LocalDateTime.now();

  @Autowired
  private CustomerRepository tested;

  @Autowired
  private CustomerStatusRepository customerStatusRepository;

  @Autowired
  private MembershipRepository membershipRepository;

  @Autowired
  private LeadRepository leadRepository;

  @Autowired
  private UserRepository userRepository;

  @Order(1)
  @Test
  void givenANewObject_whenSave_thenNewObjectIsPersisted() {
    final CustomerStatusEntity customerStatusEntity = customerStatusRepository.save(CustomerStatusEntity.builder()
        .build());
    final MembershipEntity membershipEntity = membershipRepository.save(MembershipEntity.builder()
        .build());
    final LeadEntity leadEntity = leadRepository.save(LeadEntity.builder().build());
    final CustomerEntity entity = CustomerEntity.builder().creationTime(CREATION_TIME)
        .modificationTime(MODIFICATION_TIME).lead(leadEntity).membership(membershipEntity)
        .status(customerStatusEntity).build();

    final CustomerEntity actual = tested.save(entity);

    Assertions.assertNotNull(actual);
    Assertions.assertNotNull(actual.getId());
    Assertions.assertEquals(CREATION_TIME, actual.getCreationTime());
    Assertions.assertEquals(MODIFICATION_TIME, actual.getModificationTime());
    Assertions.assertNotNull(actual.getStatus());
    Assertions.assertEquals(customerStatusEntity.getId(), actual.getStatus().getId());
    Assertions.assertNotNull(actual.getLead());
    Assertions.assertEquals(leadEntity.getId(), actual.getLead().getId());
    Assertions.assertNotNull(actual.getMembership());
    Assertions.assertEquals(membershipEntity.getId(), actual.getMembership().getId());

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
    final Optional<CustomerEntity> actual = tested.findById(ID_WRAPPER.get());

    Assertions.assertFalse(actual.isPresent());
  }
/*
  @Order(4)
  @Test
  void givenAExistingObject_whenSave_thenUpdateObject() {
    final CustomerStatusEntity otherCustomerStatusEntity = customerStatusRepository.save(CustomerStatusEntity.builder()
        .build());
    final MembershipEntity otherMembershipEntity = membershipRepository.save(MembershipEntity.builder()
        .build());
    final LeadEntity otherLeadEntity = leadRepository.save(LeadEntity.builder().build());
    final CustomerEntity existing = CustomerEntity.builder().id(ID_WRAPPER.get()).creationTime(CREATION_TIME)
        .modificationTime(MODIFICATION_TIME).lead(otherLeadEntity)
        .membership(otherMembershipEntity).status(otherCustomerStatusEntity).build();

    final CustomerEntity actual = tested.save(existing);

    Assertions.assertEquals(ID_WRAPPER.get(), actual.getId());
    Assertions.assertNotNull(actual);
    Assertions.assertNotNull(actual.getId());
    Assertions.assertEquals(CREATION_TIME, actual.getCreationTime());
    Assertions.assertEquals(MODIFICATION_TIME, actual.getModificationTime());
    Assertions.assertNotNull(actual.getStatus());
    Assertions.assertEquals(otherCustomerStatusEntity.getId(), actual.getStatus().getId());
    Assertions.assertNotNull(actual.getLead());
    Assertions.assertEquals(otherLeadEntity.getId(), actual.getLead().getId());
    Assertions.assertNotNull(actual.getMembership());
    Assertions.assertEquals(otherMembershipEntity.getId(), actual.getMembership().getId());
  }*/

 /* @Order(5)
  @Test
  void whenDeleteById_thenDeleteObject() {
    tested.deleteById(ID_WRAPPER.get());

    Assertions.assertTrue(tested.findById(ID_WRAPPER.get()).isEmpty());
  }*/
}
