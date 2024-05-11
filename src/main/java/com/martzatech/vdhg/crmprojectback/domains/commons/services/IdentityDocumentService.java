package com.martzatech.vdhg.crmprojectback.domains.commons.services;

import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.EmailNotFoundException;
import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.IdentityDocumentNotFoundException;
import com.martzatech.vdhg.crmprojectback.domains.commons.entities.PersonEntity;
import com.martzatech.vdhg.crmprojectback.domains.commons.mappers.IdentityDocumentMapper;
import com.martzatech.vdhg.crmprojectback.domains.commons.models.IdentityDocument;
import com.martzatech.vdhg.crmprojectback.domains.commons.repositories.IdentityDocumentRepository;
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
public class IdentityDocumentService {

  private IdentityDocumentRepository repository;
  private IdentityDocumentMapper mapper;

  public IdentityDocument save(final IdentityDocument model, final Integer personId) {
    if (Objects.requireNonNullElse(model.getId(), 0) != 0) {
      existsById(model.getId());
    }
    return mapper.entityToModel(
        repository.save(
            mapper.modelToEntity(model).withPerson(PersonEntity.builder().id(personId).build())
        )
    );
  }

  public List<IdentityDocument> findAll() {
    return mapper.entitiesToModelList(repository.findAll());
  }

  public IdentityDocument findById(final Integer id) {
    return mapper.entityToModel(repository.findById(id).orElseThrow(() -> new IdentityDocumentNotFoundException(id)));
  }

  public IdentityDocument findByIdAndPersonId(final Integer personId, final Integer id) {
    return mapper.entityToModel(
        repository.findByIdAndPersonId(personId, id).orElseThrow(() -> new EmailNotFoundException(id)));
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
      throw new IdentityDocumentNotFoundException(id);
    }
  }

  @Transactional
  public void keepOnlyExists(final List<Integer> ids, final Integer personId) {
    final List<Integer> idsByPersonId = repository.findByIdsByPersonId(personId);
    if (!CollectionUtils.isEmpty(idsByPersonId)) {
      final List<Integer> idsToDelete = CollectionUtils.isEmpty(ids)
          ? idsByPersonId
          : idsByPersonId.stream()
              .filter(id -> !ids.contains(id))
              .collect(Collectors.toList());
      if (!CollectionUtils.isEmpty(idsToDelete)) {
        repository.deleteAllById(idsToDelete);
        repository.flush();
        log.info("Removing not used identity documents with personId = {}. Total affected rows = [{}]", personId,
            idsToDelete);
      }
    }
  }
}
