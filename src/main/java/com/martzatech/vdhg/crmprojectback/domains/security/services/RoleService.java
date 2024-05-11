package com.martzatech.vdhg.crmprojectback.domains.security.services;

import com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants;
import com.martzatech.vdhg.crmprojectback.application.exceptions.BusinessRuleException;
import com.martzatech.vdhg.crmprojectback.application.exceptions.FieldIsDuplicatedException;
import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.RoleNotFoundException;
import com.martzatech.vdhg.crmprojectback.domains.security.mappers.RoleMapper;
import com.martzatech.vdhg.crmprojectback.domains.security.models.Role;
import com.martzatech.vdhg.crmprojectback.domains.security.repositories.RoleRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@Slf4j
@Service
public class RoleService {

  private RoleRepository repository;
  private RoleMapper mapper;

  public Role save(final Role model) {
    if (Objects.requireNonNullElse(model.getId(), 0) != 0) {
      existsById(model.getId());
      checkAdminRole(model.getId());
    } else {
      existsByName(model.getName());
    }

    return mapper.entityToModel(repository.save(mapper.modelToEntity(model)));
  }

  private static void checkAdminRole(final Integer roleId) {
    if (roleId == 1) {
      throw new BusinessRuleException("This role cannot be updated");
    }
  }

  public List<Role> findAll() {
    return mapper.entitiesToModelList(repository.findAll(Sort.by(CommonConstants.NAME_FIELD))
            .stream()
            .collect(Collectors.toList()));
  }

  public Role findById(final Integer id) {
    return mapper.entityToModel(repository.findById(id).orElseThrow(() -> new RoleNotFoundException(id)));
  }

  @Transactional
  public void deleteById(final Integer id) {
    existsById(id);
    checkAdminRole(id);

    repository.findById(id).ifPresent(entity -> repository.save(entity.withPermissions(new ArrayList<>())));

    repository.deleteById(id);
  }

  @Transactional
  public void deleteByStatusAndId(final Integer id) {
    existsById(id);
    checkAdminRole(id);

    repository.findById(id).ifPresent(entity -> repository.save(entity.withPermissions(new ArrayList<>())));

    repository.deleteRoleByStatus(id);
  }

  public void existsById(final Integer id) {
    if (!repository.existsById(id)) {
      throw new RoleNotFoundException(id);
    }
  }

  private void existsByName(final String name) {
    if (repository.existsByNameIgnoreCase(name)) {
      throw new FieldIsDuplicatedException(CommonConstants.NAME_FIELD);
    }
  }
}
