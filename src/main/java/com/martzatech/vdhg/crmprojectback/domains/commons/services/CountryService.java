package com.martzatech.vdhg.crmprojectback.domains.commons.services;

import com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants;
import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.CountryNotFoundException;
import com.martzatech.vdhg.crmprojectback.domains.commons.mappers.CountryMapper;
import com.martzatech.vdhg.crmprojectback.domains.commons.models.Country;
import com.martzatech.vdhg.crmprojectback.domains.commons.repositories.CountryRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
@Service
public class CountryService {

  private CountryRepository repository;
  private CountryMapper mapper;


  public List<Country> findAll() {
    return mapper.entitiesToModelList(repository.findAll(Sort.by(CommonConstants.NAME_FIELD)));
  }

  public Country findById(final Integer id) {
    return mapper.entityToModel(repository.findById(id).orElseThrow(() -> new CountryNotFoundException(id)));
  }

  public void existsById(final Integer id) {
    if (!repository.existsById(id)) {
      throw new CountryNotFoundException(id);
    }
  }
}
