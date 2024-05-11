package com.martzatech.vdhg.crmprojectback.domains.security.services;

import com.martzatech.vdhg.crmprojectback.domains.security.mappers.PermissionMapper;
import com.martzatech.vdhg.crmprojectback.domains.security.models.Permission;
import com.martzatech.vdhg.crmprojectback.domains.security.repositories.PermissionRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
@Service
public class PermissionService {

  private PermissionRepository repository;
  private PermissionMapper mapper;

  public List<Permission> findAll() {
    return mapper.entitiesToModelList(repository.findAll());
  }
}
