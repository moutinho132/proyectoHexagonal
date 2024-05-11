package com.martzatech.vdhg.crmprojectback.domains.commons.services;

import com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants;
import com.martzatech.vdhg.crmprojectback.domains.commons.mappers.CurrencyMapper;
import com.martzatech.vdhg.crmprojectback.domains.commons.models.Currency;
import com.martzatech.vdhg.crmprojectback.domains.commons.repositories.CurrencyRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
@Service
public class CurrencyService {

  private CurrencyRepository repository;
  private CurrencyMapper mapper;

  public List<Currency> findAll() {
    return mapper.entitiesToModelList(repository.findAll(Sort.by(CommonConstants.NAME_FIELD)));
  }
}
