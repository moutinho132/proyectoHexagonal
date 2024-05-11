package com.martzatech.vdhg.crmprojectback.domains.commons.services;

import com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants;
import com.martzatech.vdhg.crmprojectback.application.exceptions.FieldIsDuplicatedException;
import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.IdentityDocumentTypeNotFoundException;
import com.martzatech.vdhg.crmprojectback.domains.commons.entities.IdentityDocumentTypeEntity;
import com.martzatech.vdhg.crmprojectback.domains.commons.mappers.IdentityDocumentTypeMapper;
import com.martzatech.vdhg.crmprojectback.domains.commons.models.IdentityDocumentType;
import com.martzatech.vdhg.crmprojectback.domains.commons.repositories.IdentityDocumentTypeRepository;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
@Service
public class IdentityDocumentTypeService {

  private IdentityDocumentTypeRepository repository;
  private IdentityDocumentTypeMapper mapper;

  public IdentityDocumentType save(final IdentityDocumentType model) {
    if (Objects.requireNonNullElse(model.getId(), 0) != 0) {
      existsById(model.getId());
    } else {
      existsByName(model.getName());
    }

    return mapper.entityToModel(repository.save(mapper.modelToEntity(model)));
  }

  public List<IdentityDocumentType> findAll() {
    final List<IdentityDocumentTypeEntity> all = repository.findAll(Sort.by(CommonConstants.NAME_FIELD));
    return mapper.entitiesToModelList(all);
  }

  public IdentityDocumentType findById(final Integer id) {
    return mapper.entityToModel(
        repository.findById(id).orElseThrow(() -> new IdentityDocumentTypeNotFoundException(id)));
  }

  public void deleteById(final Integer id) {
    existsById(id);

    repository.deleteById(id);
  }

  private void existsById(final Integer id) {
    if (!repository.existsById(id)) {
      throw new IdentityDocumentTypeNotFoundException(id);
    }
  }

  private void existsByName(final String name) {
    if (repository.existsByNameIgnoreCase(name)) {
      throw new FieldIsDuplicatedException(CommonConstants.NAME_FIELD);
    }
  }
}
