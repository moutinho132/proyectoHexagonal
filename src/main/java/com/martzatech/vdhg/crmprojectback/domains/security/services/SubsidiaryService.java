package com.martzatech.vdhg.crmprojectback.domains.security.services;

import com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants;
import com.martzatech.vdhg.crmprojectback.application.exceptions.FieldIsDuplicatedException;
import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.SubsidiaryNotFoundException;
import com.martzatech.vdhg.crmprojectback.domains.security.mappers.SubsidiaryMapper;
import com.martzatech.vdhg.crmprojectback.domains.security.models.Subsidiary;
import com.martzatech.vdhg.crmprojectback.domains.security.repositories.SubsidiaryRepository;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
@Service
public class SubsidiaryService {

  private SubsidiaryRepository repository;
  private SubsidiaryMapper mapper;

  public Subsidiary save(final Subsidiary model) {
    if (Objects.requireNonNullElse(model.getId(), 0) != 0) {
      existsById(model.getId());
    } else {
      existsByName(model.getName());
    }

    return mapper.entityToModel(repository.save(mapper.modelToEntity(model)));
  }

  public List<Subsidiary> findAll() {
    return mapper.entitiesToModelList(repository.findAll(Sort.by(CommonConstants.NAME_FIELD)));
  }

  public Subsidiary findById(final Integer id) {
    return mapper.entityToModel(repository.findById(id).orElseThrow(() -> new SubsidiaryNotFoundException(id)));
  }

  public void existsById(final Integer id) {
    if (!repository.existsById(id)) {
      throw new SubsidiaryNotFoundException(id);
    }
  }

  public Optional<Subsidiary> findByName(final String name) {
    return repository.findByName(name);
  }

  public void deleteById(final Integer id) {
    existsById(id);

    repository.deleteById(id);
  }

  private void existsByName(final String name) {
    if (repository.existsByNameIgnoreCase(name)) {
      throw new FieldIsDuplicatedException(CommonConstants.NAME_FIELD);
    }
  }
}
