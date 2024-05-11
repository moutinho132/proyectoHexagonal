package com.martzatech.vdhg.crmprojectback.domains.security.services;

import com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants;
import com.martzatech.vdhg.crmprojectback.application.exceptions.FieldIsDuplicatedException;
import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.DepartmentNotFoundException;
import com.martzatech.vdhg.crmprojectback.domains.security.mappers.DepartmentMapper;
import com.martzatech.vdhg.crmprojectback.domains.security.models.Department;
import com.martzatech.vdhg.crmprojectback.domains.security.repositories.DepartmentRepository;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@Slf4j
@Service
public class DepartmentService {

  private DepartmentRepository repository;
  private DepartmentMapper mapper;

  public Department save(final Department model) {
    if (Objects.requireNonNullElse(model.getId(), 0) != 0) {
      existsById(model.getId());
    } else {
      existsByName(model.getName());
    }

    return mapper.entityToModel(repository.save(mapper.modelToEntity(model)));
  }

  public List<Department> findAll() {
    return mapper.entitiesToModelList(repository.findAll(Sort.by(CommonConstants.NAME_FIELD))
            .stream()
            .collect(Collectors.toList()));
  }

  public Department findById(final Integer id) {
    return mapper.entityToModel(repository.findById(id).orElseThrow(() -> new DepartmentNotFoundException(id)));
  }
  @Transactional
  public void deleteById(final Integer id) {
    existsById(id);

    repository.deleteById(id);
  }

  @Transactional
  public void deleteDepartmentByStatus(final Integer id) {
    existsById(id);

    repository.deleteDepartmentByStatus(id);
  }
  public void existsById(final Integer id) {
    if (!repository.existsById(id)) {
      throw new DepartmentNotFoundException(id);
    }
  }

  private void existsByName(final String name) {
    if (repository.existsByNameIgnoreCase(name)) {
      throw new FieldIsDuplicatedException(CommonConstants.NAME_FIELD);
    }
  }
}
