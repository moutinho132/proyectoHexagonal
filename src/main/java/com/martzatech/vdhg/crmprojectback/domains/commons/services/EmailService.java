package com.martzatech.vdhg.crmprojectback.domains.commons.services;

import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.EmailNotFoundException;
import com.martzatech.vdhg.crmprojectback.domains.commons.entities.PersonEntity;
import com.martzatech.vdhg.crmprojectback.domains.commons.mappers.EmailMapper;
import com.martzatech.vdhg.crmprojectback.domains.commons.models.Email;
import com.martzatech.vdhg.crmprojectback.domains.commons.repositories.EmailRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@AllArgsConstructor
@Slf4j
@Service
public class EmailService {

  private EmailRepository repository;
  private EmailMapper mapper;

  public Email save(final Email model, final Integer personId) {
    if (Objects.requireNonNullElse(model.getId(), 0) != 0) {
      existsById(model.getId());
    }
    return mapper.entityToModel(
        repository.save(
            mapper.modelToEntity(model).withPerson(PersonEntity.builder().id(personId).build())
        )
    );
  }

  public List<Email> findAll() {
    return mapper.entitiesToModelList(repository.findAll());
  }

  public Email findById(final Integer id) {
    return mapper.entityToModel(repository.findById(id).orElseThrow(() -> new EmailNotFoundException(id)));
  }

  public Email findByIdAndPersonId(final Integer personId, final Integer id) {
    return mapper.entityToModel(
        repository.findByIdAndPersonId(personId, id).orElseThrow(() -> new EmailNotFoundException(id)));
  }

  @Transactional
  public void keepOnlyExists(final List<Integer> emailIds, final Integer personId) {
    final List<Integer> ids = repository.findByIdsByPersonId(personId);
    if (!CollectionUtils.isEmpty(ids)) {
      final List<Integer> idsToDelete = CollectionUtils.isEmpty(emailIds)
          ? ids
          : ids.stream()
              .filter(id -> !emailIds.contains(id))
              .collect(Collectors.toList());
      if (!CollectionUtils.isEmpty(idsToDelete)) {
        repository.deleteAllById(idsToDelete);
        repository.flush();
        log.info("Removing not used emails with personId = {}. Total affected rows = {}", personId, idsToDelete);
      }
    }
  }

  public void deleteById(final Integer id) {
    existsById(id);
    repository.deleteById(id);
  }

  public void deleteByIdAndPersonId(final Integer personId, final Integer id) {
    repository.deleteByIdAndPersonId(personId, id);
  }

  private void existsById(final Integer id) {
    if (!repository.existsById(id)) {
      throw new EmailNotFoundException(id);
    }
  }
}
