package com.martzatech.vdhg.crmprojectback.domains.customers.services;

import com.martzatech.vdhg.crmprojectback.application.enums.DeletedStatusEnum;
import com.martzatech.vdhg.crmprojectback.application.enums.LeadStatusEnum;
import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.LeadNotFoundException;
import com.martzatech.vdhg.crmprojectback.application.services.PersonFieldsBlockerService;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.LeadEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.mappers.LeadMapper;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Lead;
import com.martzatech.vdhg.crmprojectback.domains.customers.repositories.LeadRepository;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.LeadSpecification;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@AllArgsConstructor
@Slf4j
@Service
public class LeadService {

  private static final Specification<LeadEntity> WITH_OUT_CONVERTED = (root, query, criteriaBuilder) ->
      criteriaBuilder.notEqual(
          root.<Integer>get("status").get("id"), LeadStatusEnum.CONVERTED.getId()
      );

  private static final Specification<LeadEntity> WITH_STATUS_ACTIVE= (root, query, criteriaBuilder) ->
          criteriaBuilder.equal(
                  root.<Integer>get("deletedStatus").get("id"), DeletedStatusEnum.ACTIVE.getId()
          );

  private LeadRepository repository;
  private LeadMapper mapper;
  private PersonFieldsBlockerService personFieldsBlockerService;

  public Lead save(final Lead model) {
    if (Objects.requireNonNullElse(model.getId(), 0) != 0) {
      existsById(model.getId());
    }

    return mapper.entityToModel(repository.save(mapper.modelToEntity(model)));
  }

  public List<Lead> findAll(final LeadSpecification specification, final Pageable pageable) {
    return (List<Lead>) personFieldsBlockerService.block(mapper.entitiesToModelList(
            repository
                .findAll(
                    Objects.nonNull(specification)
                        ? specification.and(WITH_OUT_CONVERTED).and(WITH_STATUS_ACTIVE)
                        : WITH_OUT_CONVERTED.and(WITH_STATUS_ACTIVE),
                    pageable
                ).stream()
                .toList()
        )
    );
  }

  public Long count(final LeadSpecification specification) {
    return repository.count(
        Objects.nonNull(specification)
            ? specification.and(WITH_OUT_CONVERTED)
            : WITH_OUT_CONVERTED
    );
  }

  public Lead findById(final Integer id) {
    return (Lead) personFieldsBlockerService.block(
        mapper.entityToModel(repository.findById(id).orElseThrow(() -> new LeadNotFoundException(id)))
    );
  }

  public Lead findByEmail(final String email) {
    final List<LeadEntity> byEmail = repository.findByEmail(email);
    if (CollectionUtils.isEmpty(byEmail)) {
      return null;
    }
    return mapper.entityToModel(byEmail.get(0));
  }

  public void deleteById(final Integer id) {
    existsById(id);

    repository.deleteById(id);
  }

  public void deleteStatus(final Integer id) {
    existsById(id);

    repository.deleteStatus(id);
  }

  public void existsById(final Integer id) {
    if (!repository.existsById(id)) {
      throw new LeadNotFoundException(id);
    }
  }
}
