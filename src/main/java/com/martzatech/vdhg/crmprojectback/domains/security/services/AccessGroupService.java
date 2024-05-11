package com.martzatech.vdhg.crmprojectback.domains.security.services;

import com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants;
import com.martzatech.vdhg.crmprojectback.domains.security.mappers.AccessGroupMapper;
import com.martzatech.vdhg.crmprojectback.domains.security.models.AccessGroup;
import com.martzatech.vdhg.crmprojectback.domains.security.repositories.AccessGroupRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
@Service
public class AccessGroupService {

  private AccessGroupRepository repository;
  private AccessGroupMapper mapper;

  public List<AccessGroup> findAll() {
    return mapper.entitiesToModelList(repository.findAll(Sort.by(CommonConstants.NAME_FIELD)));
  }
}
