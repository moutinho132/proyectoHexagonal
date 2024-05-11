package com.martzatech.vdhg.crmprojectback.domains.commons.services;

import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.NotFoundException;
import com.martzatech.vdhg.crmprojectback.domains.commons.entities.PhoneEntity;
import com.martzatech.vdhg.crmprojectback.domains.commons.mappers.PhoneMapper;
import com.martzatech.vdhg.crmprojectback.domains.commons.models.Phone;
import com.martzatech.vdhg.crmprojectback.domains.commons.repositories.PhoneRepository;
import java.util.List;
import java.util.Optional;

import com.martzatech.vdhg.crmprojectback.domains.customers.entities.DeletedEntity;
import com.martzatech.vdhg.crmprojectback.domains.security.models.DeletedStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyList;

@ExtendWith(MockitoExtension.class)
class PhoneServiceTests {

  private static final int ANY_ID = 1;

  @InjectMocks
  private PhoneService tested;

  @Mock
  private PhoneRepository repository;

  @Mock
  private PhoneMapper mapper;

  @Mock
  private Phone model;

  @Mock
  private PhoneEntity entity;

  /*@Test
  void givenAnNewObject_whenSave_thenSaveFromRepository() {
    Mockito.when(entity.withPerson(Mockito.any(PersonEntity.class))).thenReturn(entity);
    Mockito.when(mapper.modelToEntity(model)).thenReturn(entity);

    Assertions.assertDoesNotThrow(() -> tested.save(model, ANY_ID));

    Mockito.verify(repository).save(entity);
  }*/

  /*@Test
  void givenAnIdThatExists_whenSave_thenUpdateFromRepository() {
    Mockito.when(entity.withPerson(Mockito.any(PersonEntity.class))).thenReturn(entity);
    Mockito.when(model.getId()).thenReturn(ANY_ID);
    Mockito.when(mapper.modelToEntity(model)).thenReturn(entity);
    Mockito.when(repository.existsById(ANY_ID)).thenReturn(Boolean.TRUE);

    Assertions.assertDoesNotThrow(() -> tested.save(model, ANY_ID));

    Mockito.verify(repository).save(entity);
  }*/

  @Test
  void givenAnIdThatDoesNotExists_whenSave_thenThrowNotFoundException() {
    Mockito.when(model.getId()).thenReturn(ANY_ID);
    Mockito.when(repository.existsById(ANY_ID)).thenReturn(Boolean.FALSE);

    Assertions.assertThrows(NotFoundException.class, () -> tested.save(model, ANY_ID));

    Mockito.verify(mapper, Mockito.never()).modelToEntity(model);
    Mockito.verify(repository, Mockito.never()).save(entity);
  }

  @Test
  void whenFindAll_thenReturnList() {
    model.withDeletedStatus(DeletedStatus.builder().id(1).build());
    entity.withDeletedStatus(DeletedEntity.builder().build().builder().id(1).build());

    Mockito.when(repository.findAll()).thenReturn(List.of(entity));
    Mockito.when(mapper.entitiesToModelList(anyList())).thenReturn(List.of(model));

    final List<Phone> actual = tested.findAll();

    Assertions.assertEquals(List.of(model), actual);
  }

  @Test
  void whenFindById_thenReturnAnModelObject() {
    Mockito.when(repository.findById(ANY_ID)).thenReturn(Optional.of(entity));
    Mockito.when(mapper.entityToModel(entity)).thenReturn(model);

    final Phone actual = tested.findById(ANY_ID);

    Assertions.assertEquals(model, actual);
  }

  @Test
  void givenAnIdThatDoesNotExists_whenFindById_thenThrowNotFoundException() {
    Mockito.when(repository.findById(ANY_ID)).thenReturn(Optional.empty());

    Assertions.assertThrows(NotFoundException.class, () -> tested.findById(ANY_ID));

    Mockito.verify(mapper, Mockito.never()).entityToModel(entity);
  }

  @Test
  void whenDeleteById_thenDeleteByIdFromRepository() {
    Mockito.when(repository.existsById(ANY_ID)).thenReturn(Boolean.TRUE);

    Assertions.assertDoesNotThrow(() -> tested.deleteById(ANY_ID));

    Mockito.verify(repository).deleteById(ANY_ID);
  }

  @Test
  void givenAnIdThatDoesNotExists_whenDeleteById_thenThrowNotFoundException() {
    Mockito.when(repository.existsById(ANY_ID)).thenReturn(Boolean.FALSE);

    Assertions.assertThrows(NotFoundException.class, () -> tested.deleteById(ANY_ID));

    Mockito.verify(repository, Mockito.never()).deleteById(ANY_ID);
  }
}
