package com.martzatech.vdhg.crmprojectback.domains.customers.services;

import com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants;
import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.PaymentMethodNotFoundException;
import com.martzatech.vdhg.crmprojectback.domains.customers.mappers.PaymentMethodMapper;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.PaymentMethod;
import com.martzatech.vdhg.crmprojectback.domains.customers.repositories.PaymentMethodRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
@Service
public class PaymentMethodService {

  private PaymentMethodRepository repository;
  private PaymentMethodMapper mapper;

  public List<PaymentMethod> findAll() {
    return mapper.entitiesToModelList(repository.findAll(Sort.by(CommonConstants.NAME_FIELD)));
  }

  public void existsById(final Integer id) {
    if (!repository.existsById(id)) {
      throw new PaymentMethodNotFoundException(id);
    }
  }
}
