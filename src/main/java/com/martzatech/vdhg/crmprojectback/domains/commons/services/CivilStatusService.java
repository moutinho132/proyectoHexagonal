package com.martzatech.vdhg.crmprojectback.domains.commons.services;

import com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants;
import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.CivilStatusNotFoundException;
import com.martzatech.vdhg.crmprojectback.application.exceptions.FieldIsDuplicatedException;
import com.martzatech.vdhg.crmprojectback.domains.commons.entities.CivilStatusEntity;
import com.martzatech.vdhg.crmprojectback.domains.commons.mappers.CivilStatusMapper;
import com.martzatech.vdhg.crmprojectback.domains.commons.models.CivilStatus;
import com.martzatech.vdhg.crmprojectback.domains.commons.repositories.CivilStatusRepository;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
@Service
public class CivilStatusService {

  private CivilStatusRepository repository;
  private CivilStatusMapper mapper;

  public CivilStatus save(final CivilStatus model) {
    if (Objects.requireNonNullElse(model.getId(), 0) != 0) {
      existsById(model.getId());
    } else {
      existsByName(model.getName());
    }

    return mapper.entityToModel(repository.save(mapper.modelToEntity(model)));
  }

  public List<CivilStatus> findAll() {
    final List<CivilStatusEntity> all = repository.findAll(Sort.by(CommonConstants.NAME_FIELD));
    return mapper.entitiesToModelList(all);
  }

  public CivilStatus findById(final Integer id) {
    return mapper.entityToModel(repository.findById(id).orElseThrow(() -> new CivilStatusNotFoundException(id)));
  }

  public void deleteById(final Integer id) {
    existsById(id);
    repository.deleteById(id);
  }

  public void existsById(final Integer id) {
    if (!repository.existsById(id)) {
      throw new CivilStatusNotFoundException(id);
    }
  }

  private void existsByName(final String name) {
    if (repository.existsByNameIgnoreCase(name)) {
      throw new FieldIsDuplicatedException(CommonConstants.NAME_FIELD);
    }
  }
}
