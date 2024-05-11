package com.martzatech.vdhg.crmprojectback.domains.commons.services;

import com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants;
import com.martzatech.vdhg.crmprojectback.application.exceptions.FieldIsDuplicatedException;
import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.PersonTitleNotFoundException;
import com.martzatech.vdhg.crmprojectback.domains.commons.entities.PersonTitleEntity;
import com.martzatech.vdhg.crmprojectback.domains.commons.mappers.PersonTitleMapper;
import com.martzatech.vdhg.crmprojectback.domains.commons.models.PersonTitle;
import com.martzatech.vdhg.crmprojectback.domains.commons.repositories.PersonTitleRepository;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
@Service
public class PersonTitleService {

  private PersonTitleRepository repository;
  private PersonTitleMapper mapper;

  public PersonTitle save(final PersonTitle model) {
    if (Objects.requireNonNullElse(model.getId(), 0) != 0) {
      existsById(model.getId());
    } else {
      existsByName(model.getName());
    }

    return mapper.entityToModel(repository.save(mapper.modelToEntity(model)));
  }

  public List<PersonTitle> findAll() {
    final List<PersonTitleEntity> all = repository.findAll(Sort.by(CommonConstants.NAME_FIELD));
    return mapper.entitiesToModelList(all);
  }

  public PersonTitle findById(final Integer id) {
    return mapper.entityToModel(repository.findById(id).orElseThrow(() -> new PersonTitleNotFoundException(id)));
  }

  public void deleteById(final Integer id) {
    existsById(id);
    repository.deleteById(id);
  }

  public void existsById(final Integer id) {
    if (!repository.existsById(id)) {
      throw new PersonTitleNotFoundException(id);
    }
  }

  private void existsByName(final String name) {
    if (repository.existsByNameIgnoreCase(name)) {
      throw new FieldIsDuplicatedException(CommonConstants.NAME_FIELD);
    }
  }
}
