package com.martzatech.vdhg.crmprojectback.domains.customers.services;

import com.martzatech.vdhg.crmprojectback.application.enums.DeletedStatusEnum;
import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.CorporateNotFoundException;
import com.martzatech.vdhg.crmprojectback.application.helper.SpecificationHelper;
import com.martzatech.vdhg.crmprojectback.domains.customers.mappers.CorporateMapper;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Corporate;
import com.martzatech.vdhg.crmprojectback.domains.customers.repositories.CorporateRepository;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.CorporateSpecification;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
@Service
public class CorporateService {

  private CorporateRepository repository;
  private CorporateMapper mapper;

  public Corporate save(final Corporate model) {
    if (Objects.requireNonNullElse(model.getId(), 0) != 0) {
      existsById(model.getId());
    }

    return mapper.entityToModel(repository.save(mapper.modelToEntity(model)));
  }

  public List<Corporate> findAll(final CorporateSpecification specification, final Pageable pageable) {
    return mapper.entitiesToModelList(repository.findAll(SpecificationHelper
            .addSoftDeleteCorporate(DeletedStatusEnum.ACTIVE.getId(),specification), pageable).toList());
  }

  public Long count(final CorporateSpecification specification) {
    return repository.count(SpecificationHelper.addSoftDeleteCorporate(DeletedStatusEnum.ACTIVE.getId(),specification));
  }

  public Corporate findById(final Integer id) {
    return mapper.entityToModel(repository.findById(id).orElseThrow(() -> new CorporateNotFoundException(id)));
  }

  public void deleteById(final Integer id) {
    existsById(id);

    repository.deleteById(id);
  }

  public void deleteStatus(final Integer id) {
    existsById(id);

    repository.deleteStatus(id);
  }

  private void existsById(final Integer id) {
    if (!repository.existsById(id)) {
      throw new CorporateNotFoundException(id);
    }
  }
}
