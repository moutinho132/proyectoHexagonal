package com.martzatech.vdhg.crmprojectback.domains.commons.services;

import com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants;
import com.martzatech.vdhg.crmprojectback.application.exceptions.FieldIsDuplicatedException;
import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.NotFoundException;
import com.martzatech.vdhg.crmprojectback.domains.commons.entities.GenderEntity;
import com.martzatech.vdhg.crmprojectback.domains.commons.mappers.GenderMapper;
import com.martzatech.vdhg.crmprojectback.domains.commons.models.Gender;
import com.martzatech.vdhg.crmprojectback.domains.commons.repositories.GenderRepository;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
@Service
public class GenderService {

  private GenderRepository repository;
  private GenderMapper mapper;

  public Gender save(final Gender model) {
    if (Objects.requireNonNullElse(model.getId(), 0) != 0) {
      existsById(model.getId());
    } else {
      existsByName(model.getName());
    }

    return mapper.entityToModel(repository.save(mapper.modelToEntity(model)));
  }

  public List<Gender> findAll() {
    final List<GenderEntity> all = repository.findAll(Sort.by(CommonConstants.NAME_FIELD));
    return mapper.entitiesToModelList(all);
  }

  public Gender findById(final Integer id) {
    return mapper.entityToModel(repository.findById(id).orElseThrow(NotFoundException::new));
  }

  public void deleteById(final Integer id) {
    existsById(id);

    repository.deleteById(id);
  }

  private void existsById(final Integer id) {
    if (!repository.existsById(id)) {
      throw new NotFoundException();
    }
  }

  private void existsByName(final String name) {
    if (repository.existsByNameIgnoreCase(name)) {
      throw new FieldIsDuplicatedException(CommonConstants.NAME_FIELD);
    }
  }
}
