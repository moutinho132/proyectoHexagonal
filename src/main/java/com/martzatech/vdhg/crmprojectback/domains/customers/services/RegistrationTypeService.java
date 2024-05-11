package com.martzatech.vdhg.crmprojectback.domains.customers.services;

import com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants;
import com.martzatech.vdhg.crmprojectback.application.exceptions.FieldIsDuplicatedException;
import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.RegistrationTypeNotFoundException;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.RegistrationTypeEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.mappers.RegistrationTypeMapper;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.RegistrationType;
import com.martzatech.vdhg.crmprojectback.domains.customers.repositories.RegistrationTypeRepository;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
@Service
public class RegistrationTypeService {

  private RegistrationTypeRepository repository;
  private RegistrationTypeMapper mapper;

  public RegistrationType save(final RegistrationType model) {
    if (Objects.requireNonNullElse(model.getId(), 0) != 0) {
      existsById(model.getId());
    } else {
      existsByName(model.getName());
    }

    return mapper.entityToModel(repository.save(mapper.modelToEntity(model)));
  }

  public List<RegistrationType> findAll() {
    final List<RegistrationTypeEntity> all = repository.findAll(Sort.by(CommonConstants.NAME_FIELD));
    return mapper.entitiesToModelList(all);
  }

  public RegistrationType findById(final Integer id) {
    return mapper.entityToModel(repository.findById(id).orElseThrow(() -> new RegistrationTypeNotFoundException(id)));
  }

  public void deleteById(final Integer id) {
    existsById(id);

    repository.deleteById(id);
  }

  public void existsById(final Integer id) {
    if (!repository.existsById(id)) {
      throw new RegistrationTypeNotFoundException(id);
    }
  }

  private void existsByName(final String name) {
    if (repository.existsByNameIgnoreCase(name)) {
      throw new FieldIsDuplicatedException(CommonConstants.NAME_FIELD);
    }
  }
}
