package com.martzatech.vdhg.crmprojectback.domains.security.repositories;

import com.martzatech.vdhg.crmprojectback.Application;
import com.martzatech.vdhg.crmprojectback.domains.security.entities.DepartmentEntity;
import com.martzatech.vdhg.crmprojectback.domains.security.entities.RoleEntity;
import com.martzatech.vdhg.crmprojectback.domains.security.entities.UserEntity;
import java.util.List;
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
class UserRepositoryITests {

  private static final AtomicInteger ID_WRAPPER = new AtomicInteger();
  private static final String NAME = "NAME";
  private static final String OTHER_NAME = "OTHER_NAME";
  private static final String ANY_STRING = "AS";
  private static final String OTHER_ANY_STRING = "AS";

  @Autowired
  private UserRepository tested;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private DepartmentRepository departmentRepository;

  @Order(1)
  @Test
  void givenANewObject_whenSave_thenNewObjectIsPersisted() {
    final DepartmentEntity departmentEntity = departmentRepository.save(DepartmentEntity.builder().name(NAME)
        .description(ANY_STRING).build());
    final RoleEntity roleEntity = roleRepository.save(
        RoleEntity.builder().name(NAME).department(departmentEntity).build());
    final UserEntity entity = UserEntity.builder().name(NAME).roles(List.of(roleEntity)).email(ANY_STRING)
        .title(ANY_STRING).address(ANY_STRING).mobile(ANY_STRING).surname(ANY_STRING).address(ANY_STRING).build();

    final UserEntity actual = tested.save(entity);

    Assertions.assertNotNull(actual);
    Assertions.assertNotNull(actual.getId());
    Assertions.assertEquals(NAME, actual.getName());
    Assertions.assertEquals(ANY_STRING, actual.getSurname());
    Assertions.assertEquals(ANY_STRING, actual.getTitle());
    Assertions.assertEquals(ANY_STRING, actual.getEmail());
    Assertions.assertEquals(ANY_STRING, actual.getMobile());
    Assertions.assertEquals(ANY_STRING, actual.getAddress());
    Assertions.assertFalse(CollectionUtils.isEmpty(actual.getRoles()));
    Assertions.assertEquals(1, actual.getRoles().size());
    Assertions.assertEquals(roleEntity.getId(), actual.getRoles().get(0).getId());

    ID_WRAPPER.set(actual.getId());
  }

  /*@Order(2)
  @Test
  void whenFindAll_thenReturnAnOnlyObject() {
    Assertions.assertFalse(CollectionUtils.isEmpty(tested.findAll()));
  }*/
/*
  @Order(3)
  @Test
  void whenFindById_thenReturnPersistedObject() {
    final Optional<UserEntity> actual = tested.findById(ID_WRAPPER.get());

    Assertions.assertTrue(actual.isPresent());
  }*/

  /*@Order(4)
  @Test
  void givenAExistingObject_whenSave_thenUpdateObject() {
    final DepartmentEntity otherDepartment = departmentRepository.save(DepartmentEntity.builder().name(NAME)
        .description(ANY_STRING).build());
    final RoleEntity otherRoleEntity = roleRepository.save(RoleEntity.builder().name(ANY_STRING)
        .department(otherDepartment).build());
    final UserEntity existing = UserEntity.builder().id(ID_WRAPPER.get()).roles(List.of(otherRoleEntity))
        .name(OTHER_NAME).email(OTHER_ANY_STRING).title(OTHER_ANY_STRING).address(OTHER_ANY_STRING)
        .mobile(OTHER_ANY_STRING).surname(OTHER_ANY_STRING).address(OTHER_ANY_STRING).build();

    final UserEntity actual = tested.save(existing);

    Assertions.assertEquals(ID_WRAPPER.get(), actual.getId());
    Assertions.assertEquals(OTHER_NAME, actual.getName());
    Assertions.assertEquals(OTHER_ANY_STRING, actual.getSurname());
    Assertions.assertEquals(OTHER_ANY_STRING, actual.getTitle());
    Assertions.assertEquals(OTHER_ANY_STRING, actual.getEmail());
    Assertions.assertEquals(OTHER_ANY_STRING, actual.getMobile());
    Assertions.assertEquals(OTHER_ANY_STRING, actual.getAddress());
    Assertions.assertFalse(CollectionUtils.isEmpty(actual.getRoles()));
    Assertions.assertEquals(1, actual.getRoles().size());
    Assertions.assertEquals(otherRoleEntity.getId(), actual.getRoles().get(0).getId());
  }*/

  /*@Order(5)
  @Test
  void whenDeleteById_thenDeleteObject() {
    tested.deleteById(ID_WRAPPER.get());

    Assertions.assertTrue(tested.findById(ID_WRAPPER.get()).isEmpty());
  }*/
}
