package com.martzatech.vdhg.crmprojectback.domains.customers.repositories;

import com.martzatech.vdhg.crmprojectback.Application;
import com.martzatech.vdhg.crmprojectback.domains.commons.entities.PersonEntity;
import com.martzatech.vdhg.crmprojectback.domains.commons.repositories.PersonRepository;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.CompanyEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.LeadEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.LeadStatusEntity;
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
class LeadRepositoryITests {

  private static final AtomicInteger ID_WRAPPER = new AtomicInteger();
  private static final String NAME = "NAME";
  private static final String OTHER_NAME = "OTHER_NAME";

  @Autowired
  private LeadRepository tested;

  @Autowired
  private LeadStatusRepository leadStatusRepository;

  @Autowired
  private CompanyRepository companyRepository;

  @Autowired
  private PersonRepository personRepository;

  @Order(1)
  @Test
  void givenANewObject_whenSave_thenNewObjectIsPersisted() {
    final LeadStatusEntity leadStatusEntity = leadStatusRepository.save(LeadStatusEntity.builder().build());
    final CompanyEntity companyEntity = companyRepository.save(CompanyEntity.builder().build());
    final PersonEntity personEntity = personRepository.save(PersonEntity.builder().build());
    final LeadEntity entity = LeadEntity.builder().status(leadStatusEntity).company(companyEntity).person(personEntity)
        .build();

    final LeadEntity actual = tested.save(entity);

    Assertions.assertNotNull(actual);
    Assertions.assertNotNull(actual.getId());
    Assertions.assertNotNull(actual.getStatus());
    Assertions.assertEquals(leadStatusEntity.getId(), actual.getStatus().getId());
    Assertions.assertNotNull(actual.getCompany());
    Assertions.assertEquals(companyEntity.getId(), actual.getCompany().getId());
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
    final Optional<LeadEntity> actual = tested.findById(ID_WRAPPER.get());

    Assertions.assertFalse(actual.isPresent());
  }
/*
  @Order(4)
  @Test
  void givenAExistingObject_whenSave_thenUpdateObject() {
    final LeadStatusEntity otherLeadStatusEntity = leadStatusRepository.save(LeadStatusEntity.builder().build());
    final CompanyEntity otherCompanyEntity = companyRepository.save(CompanyEntity.builder().build());
    final PersonEntity otherPersonEntity = personRepository.save(PersonEntity.builder().build());
    final LeadEntity existing = LeadEntity.builder().id(ID_WRAPPER.get()).status(otherLeadStatusEntity)
        .company(otherCompanyEntity).person(otherPersonEntity).build();

    final LeadEntity actual = tested.save(existing);

    Assertions.assertEquals(ID_WRAPPER.get(), actual.getId());
    Assertions.assertEquals(otherLeadStatusEntity.getId(), actual.getStatus().getId());
    Assertions.assertNotNull(actual.getCompany());
    Assertions.assertEquals(otherCompanyEntity.getId(), actual.getCompany().getId());
    Assertions.assertNotNull(actual.getPerson());
    Assertions.assertEquals(otherPersonEntity.getId(), actual.getPerson().getId());
  }*/

  /*@Order(5)
  @Test
  void whenDeleteById_thenDeleteObject() {
    tested.deleteById(ID_WRAPPER.get());

    Assertions.assertTrue(tested.findById(ID_WRAPPER.get()).isEmpty());
  }*/
}
