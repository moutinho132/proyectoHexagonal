package com.martzatech.vdhg.crmprojectback.domains.customers.services;

import com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants;
import com.martzatech.vdhg.crmprojectback.application.exceptions.FieldIsDuplicatedException;
import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.MembershipTypeNotFoundException;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.MembershipTypeEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.mappers.MembershipTypeMapper;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.MembershipType;
import com.martzatech.vdhg.crmprojectback.domains.customers.repositories.MembershipTypeRepository;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
@Service
public class MembershipTypeService {

  private MembershipTypeRepository repository;
  private MembershipTypeMapper mapper;

  public MembershipType save(final MembershipType model) {
    if (Objects.requireNonNullElse(model.getId(), 0) != 0) {
      existsById(model.getId());
    } else {
      existsByName(model.getName());
    }

    return mapper.entityToModel(repository.save(mapper.modelToEntity(model)));
  }

  public List<MembershipType> findAll() {
    final List<MembershipTypeEntity> all = repository.findAll(Sort.by(CommonConstants.NAME_FIELD));
    return mapper.entitiesToModelList(all);
  }

  public MembershipType findById(final Integer id) {
    return mapper.entityToModel(repository.findById(id).orElseThrow(() -> new MembershipTypeNotFoundException(id)));
  }

  public void deleteById(final Integer id) {
    existsById(id);

    repository.deleteById(id);
  }

  public void existsById(final Integer id) {
    if (!repository.existsById(id)) {
      throw new MembershipTypeNotFoundException(id);
    }
  }

  private void existsByName(final String name) {
    if (repository.existsByNameIgnoreCase(name)) {
      throw new FieldIsDuplicatedException(CommonConstants.NAME_FIELD);
    }
  }
}
