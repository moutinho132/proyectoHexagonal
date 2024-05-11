package com.martzatech.vdhg.crmprojectback.domains.customers.services;

import com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants;
import com.martzatech.vdhg.crmprojectback.application.exceptions.FieldIsDuplicatedException;
import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.MembershipNotFoundException;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.MembershipEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.mappers.MembershipMapper;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Membership;
import com.martzatech.vdhg.crmprojectback.domains.customers.repositories.MembershipRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Slf4j
@Service
public class MembershipService {

  private MembershipRepository repository;
  private MembershipMapper mapper;

  public Membership save(final Membership model) {
    if (Objects.requireNonNullElse(model.getId(), 0) != 0) {
      existsById(model.getId());
    } else {
      existsByName(model.getName());
    }

    return mapper.entityToModel(repository.save(mapper.modelToEntity(model)));
  }
  @Transactional
  public List<Membership> findAll() {
    final List<MembershipEntity> all = repository.findAll(Sort.by(CommonConstants.NAME_FIELD));
    return mapper.entitiesToModelList(all);
  }

  public Membership findById(final Integer id) {
    return mapper.entityToModel(repository.findById(id).orElseThrow(() -> new MembershipNotFoundException(id)));
  }

  public void deleteById(final Integer id) {
    existsById(id);

    repository.deleteById(id);
  }

  public void existsById(final Integer id) {
    if (!repository.existsById(id)) {
      throw new MembershipNotFoundException(id);
    }
  }

  private void existsByName(final String name) {
    if (repository.existsByNameIgnoreCase(name)) {
      throw new FieldIsDuplicatedException(CommonConstants.NAME_FIELD);
    }
  }
}
