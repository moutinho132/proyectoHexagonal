package com.martzatech.vdhg.crmprojectback.domains.customers.services;

import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.LeadCustomerFileNotFoundException;
import com.martzatech.vdhg.crmprojectback.domains.customers.mappers.LeadCustomerFileMapper;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.LeadCustomerFile;
import com.martzatech.vdhg.crmprojectback.domains.customers.repositories.LeadCustomerFileRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
@Service
public class  LeadCustomerFileService {

  private LeadCustomerFileRepository repository;
  private LeadCustomerFileMapper mapper;

  public LeadCustomerFile save(final LeadCustomerFile model) {
    return mapper.entityToModel(repository.save(mapper.modelToEntity(model)));
  }

  public LeadCustomerFile findById(final Integer id) {
    return repository.findById(id).map(mapper::entityToModel)
            .orElseThrow(() -> new LeadCustomerFileNotFoundException(id));
  }

  public void deleteById(final Integer id) {
    existsById(id);

    repository.deleteById(id);
  }

  public void existsById(final Integer id) {
    if (!repository.existsById(id)) {
      throw new LeadCustomerFileNotFoundException(id);
    }
  }
}
