package com.martzatech.vdhg.crmprojectback.domains.customers.services;

import com.martzatech.vdhg.crmprojectback.domains.customers.entities.EmployeeTypeEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.mappers.EmployeeTypeMapper;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.EmployeeType;
import com.martzatech.vdhg.crmprojectback.domains.customers.repositories.EmployeeTypeRepository;
import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.NotFoundException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmployeeTypeServiceTests {

  private static final int ANY_ID = 1;

  @InjectMocks
  private EmployeeTypeService tested;

  @Mock
  private EmployeeTypeRepository repository;

  @Mock
  private EmployeeTypeMapper mapper;

  @Mock
  private EmployeeType model;

  @Mock
  private EmployeeTypeEntity entity;

  @Test
  void givenAnNewObject_whenSave_thenSaveFromRepository() {
    Mockito.when(mapper.modelToEntity(model)).thenReturn(entity);

    Assertions.assertDoesNotThrow(() -> tested.save(model));

    Mockito.verify(repository).save(entity);
  }

  @Test
  void givenAnIdThatExists_whenSave_thenUpdateFromRepository() {
    Mockito.when(model.getId()).thenReturn(ANY_ID);
    Mockito.when(mapper.modelToEntity(model)).thenReturn(entity);
    Mockito.when(repository.existsById(ANY_ID)).thenReturn(Boolean.TRUE);

    Assertions.assertDoesNotThrow(() -> tested.save(model));

    Mockito.verify(repository).save(entity);
  }

  @Test
  void givenAnIdThatDoesNotExists_whenSave_thenThrowNotFoundException() {
    Mockito.when(model.getId()).thenReturn(ANY_ID);
    Mockito.when(repository.existsById(ANY_ID)).thenReturn(Boolean.FALSE);

    Assertions.assertThrows(NotFoundException.class, () -> tested.save(model));

    Mockito.verify(mapper, Mockito.never()).modelToEntity(model);
    Mockito.verify(repository, Mockito.never()).save(entity);
  }

  @Test
  void whenFindAll_thenReturnList() {
    Mockito.when(repository.findAll()).thenReturn(List.of(entity));
    Mockito.when(mapper.entitiesToModelList(List.of(entity))).thenReturn(List.of(model));

    final List<EmployeeType> actual = tested.findAll();

    Assertions.assertEquals(List.of(model), actual);
  }

  @Test
  void whenFindById_thenReturnAnModelObject() {
    Mockito.when(repository.findById(ANY_ID)).thenReturn(Optional.of(entity));
    Mockito.when(mapper.entityToModel(entity)).thenReturn(model);

    final EmployeeType actual = tested.findById(ANY_ID);

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
