package com.martzatech.vdhg.crmprojectback.domains.commons.services;

import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.PersonNotFoundException;
import com.martzatech.vdhg.crmprojectback.domains.commons.entities.PersonEntity;
import com.martzatech.vdhg.crmprojectback.domains.commons.mappers.PersonMapper;
import com.martzatech.vdhg.crmprojectback.domains.commons.models.Person;
import com.martzatech.vdhg.crmprojectback.domains.commons.repositories.PersonRepository;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
@Service
public class PersonService {

  private PersonRepository repository;
  private PersonMapper mapper;

  public Person save(final Person model) {
    if (Objects.requireNonNullElse(model.getId(), 0) != 0) {
      existsById(model.getId());
    }

    final PersonEntity entity = mapper.modelToEntity(model);
    return mapper.entityToModel(repository.save(entity));
  }

  public List<Person> findAll() {
    return mapper.entitiesToModelList(repository.findAll());
  }

  public Person findById(final Integer id) {
    return mapper.entityToModel(repository.findById(id).orElseThrow(() -> new PersonNotFoundException(id)));
  }

  public void deleteById(final Integer id) {
    existsById(id);

    repository.deleteById(id);
  }

  public void existsById(final Integer id) {
    if (!repository.existsById(id)) {
      throw new PersonNotFoundException(id);
    }
  }
}
