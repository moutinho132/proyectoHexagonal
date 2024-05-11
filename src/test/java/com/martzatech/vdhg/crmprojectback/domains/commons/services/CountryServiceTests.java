package com.martzatech.vdhg.crmprojectback.domains.commons.services;

import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.NotFoundException;
import com.martzatech.vdhg.crmprojectback.domains.commons.entities.CountryEntity;
import com.martzatech.vdhg.crmprojectback.domains.commons.mappers.CountryMapper;
import com.martzatech.vdhg.crmprojectback.domains.commons.models.Country;
import com.martzatech.vdhg.crmprojectback.domains.commons.repositories.CountryRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
class CountryServiceTests {

  private static final int ANY_ID = 1;

  @InjectMocks
  private CountryService tested;

  @Mock
  private CountryRepository repository;

  @Mock
  private CountryMapper mapper;

  @Mock
  private Country model;

  @Mock
  private CountryEntity entity;

  @Test
  void whenFindAll_thenReturnList() {
    Mockito.when(repository.findAll(Mockito.any(Sort.class))).thenReturn(List.of(entity));
    Mockito.when(mapper.entitiesToModelList(List.of(entity))).thenReturn(List.of(model));

    final List<Country> actual = tested.findAll();

    Assertions.assertEquals(List.of(model), actual);
  }

  @Test
  void whenFindById_thenReturnAnModelObject() {
    Mockito.when(repository.findById(ANY_ID)).thenReturn(Optional.of(entity));
    Mockito.when(mapper.entityToModel(entity)).thenReturn(model);

    final Country actual = tested.findById(ANY_ID);

    Assertions.assertEquals(model, actual);
  }

  @Test
  void givenAnIdThatDoesNotExists_whenFindById_thenThrowNotFoundException() {
    Mockito.when(repository.findById(ANY_ID)).thenReturn(Optional.empty());

    Assertions.assertThrows(NotFoundException.class, () -> tested.findById(ANY_ID));

    Mockito.verify(mapper, Mockito.never()).entityToModel(entity);
  }
}
