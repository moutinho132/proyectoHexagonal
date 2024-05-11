package com.martzatech.vdhg.crmprojectback.domains.customers.services;

import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.AssociatedNotFoundException;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.GroupAccountEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.mappers.AssociatedMapper;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Associated;
import com.martzatech.vdhg.crmprojectback.domains.customers.repositories.AssociatedRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Slf4j
@Service
public class AssociatedService {

  private AssociatedRepository repository;
  private AssociatedMapper mapper;

  public Associated save(final Associated model) {
    if (Objects.requireNonNullElse(model.getId(), 0) != 0) {
      existsById(model.getId());
    }

    return mapper.entityToModel(repository.save(mapper.modelToEntity(model)));
  }

  public Associated findById(final Integer id) {
    return mapper.entityToModel(repository.findById(id).orElseThrow(() -> new AssociatedNotFoundException(id)));
  }

  public void deleteById(final Integer id) {
    existsById(id);

    repository.deleteById(id);
  }

  private void existsById(final Integer id) {
    if (!repository.existsById(id)) {
      throw new AssociatedNotFoundException(id);
    }
  }

  public Associated findByEmail(final String email) {
    return repository.findByEmail(email).map(mapper::entityToModel).orElse(null);
  }

  public void updateMainContactToFalseByGroupAccountId(final Integer groupAccountId) {
    repository.updateMainContactToFalseByGroupAccountId(groupAccountId);
  }

  public List<Associated> findByAssociatedGroupAccount(final GroupAccountEntity accountEntity){
    return mapper.entitiesToModelList(repository.findByGroupAccount(accountEntity));
  }
}
