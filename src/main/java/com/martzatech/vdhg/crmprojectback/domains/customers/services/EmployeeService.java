package com.martzatech.vdhg.crmprojectback.domains.customers.services;

import com.martzatech.vdhg.crmprojectback.domains.customers.mappers.EmployeeMapper;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Employee;
import com.martzatech.vdhg.crmprojectback.domains.customers.repositories.EmployeeRepository;
import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.NotFoundException;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
@Service
public class EmployeeService {

  private EmployeeRepository repository;
  private EmployeeMapper mapper;

  public void save(final Employee model) {
    if (Objects.requireNonNullElse(model.getId(), 0) != 0) {
      existsById(model.getId());
    }

    repository.save(mapper.modelToEntity(model));
  }

  public List<Employee> findAll() {
    return mapper.entitiesToModelList(repository.findAll());
  }

  public Employee findById(final Integer id) {
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
