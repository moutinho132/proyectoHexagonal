package com.martzatech.vdhg.crmprojectback.domains.customers.services;

import com.martzatech.vdhg.crmprojectback.domains.customers.mappers.EmployeeTypeMapper;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.EmployeeType;
import com.martzatech.vdhg.crmprojectback.domains.customers.repositories.EmployeeTypeRepository;
import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.NotFoundException;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
@Service
public class EmployeeTypeService {

  private EmployeeTypeRepository repository;
  private EmployeeTypeMapper mapper;

  public void save(final EmployeeType model) {
    if (Objects.requireNonNullElse(model.getId(), 0) != 0) {
      existsById(model.getId());
    }

    repository.save(mapper.modelToEntity(model));
  }

  public List<EmployeeType> findAll() {
    return mapper.entitiesToModelList(repository.findAll());
  }

  public EmployeeType findById(final Integer id) {
    return mapper.entityToModel(repository.findById(id).orElseThrow(NotFoundException::new));
  }

  public void deleteById(final Integer id) {
    existsById(id);

    repository.deleteById(id);
  }

  private void existsById(final Integer id) {
    if (!repository.existsById(id)) {
      throw new NotFoundException();
    }
  }
}
