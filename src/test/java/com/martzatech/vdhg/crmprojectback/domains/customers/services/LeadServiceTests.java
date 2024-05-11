package com.martzatech.vdhg.crmprojectback.domains.customers.services;

import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.NotFoundException;
import com.martzatech.vdhg.crmprojectback.application.services.PersonFieldsBlockerService;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.DeletedEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.LeadEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.mappers.LeadMapper;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Lead;
import com.martzatech.vdhg.crmprojectback.domains.customers.repositories.LeadRepository;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.LeadSpecification;
import java.util.List;
import java.util.Optional;

import com.martzatech.vdhg.crmprojectback.domains.security.models.DeletedStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;

@ExtendWith(MockitoExtension.class)
class LeadServiceTests {

  private static final int ANY_ID = 1;

  @InjectMocks
  private LeadService tested;

  @Mock
  private LeadRepository repository;

  @Mock
  private LeadMapper mapper;

  @Mock
  private PersonFieldsBlockerService personFieldsBlockerService;

  @Mock
  private Lead model;

  @Mock
  private LeadEntity entity;

  @Mock
  private LeadSpecification leadSpecification;

  @Mock
  private Pageable pageable;

  @Mock
  private Page page;

  @Mock
  private List withFieldsBlocked;

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
    model.withDeletedStatus(DeletedStatus.builder().id(1).build());
    entity.withDeletedStatus(DeletedEntity.builder().build().builder().id(1).build());
    Mockito.when(repository.findAll(leadSpecification, pageable)).thenReturn(page);
    Mockito.when(mapper.entitiesToModelList(anyList())).thenReturn(List.of(model));
    Mockito.when(leadSpecification.and(any())).thenReturn(leadSpecification);
    Mockito.when(personFieldsBlockerService.block(List.of(model))).thenReturn(withFieldsBlocked);

    final List<Lead> actual = tested.findAll(leadSpecification, pageable);

    Assertions.assertEquals(withFieldsBlocked, actual);
  }

  @Test
  void whenFindById_thenReturnAnModelObject() {
    Mockito.when(repository.findById(ANY_ID)).thenReturn(Optional.of(entity));
    Mockito.when(mapper.entityToModel(entity)).thenReturn(model);
    Mockito.when(personFieldsBlockerService.block(model)).thenReturn(model);

    final Lead actual = tested.findById(ANY_ID);

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
