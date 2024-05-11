package com.martzatech.vdhg.crmprojectback.domains.customers.services;

import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.CountryNotFoundException;
import com.martzatech.vdhg.crmprojectback.domains.customers.mappers.CompanyMapper;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Company;
import com.martzatech.vdhg.crmprojectback.domains.customers.repositories.CompanyRepository;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
@Service
public class DiscountService {

  private CompanyRepository repository;
  private CompanyMapper mapper;

  public Company save(final Company model) {
    if (Objects.requireNonNullElse(model.getId(), 0) != 0) {
      existsById(model.getId());
    }

    return mapper.entityToModel(repository.save(mapper.modelToEntity(model)));
  }

  public List<Company> findAll() {
    return mapper.entitiesToModelList(repository.findAll());
  }

  public Company findById(final Integer id) {
    return mapper.entityToModel(repository.findById(id).orElseThrow(() -> new CountryNotFoundException(id)));
  }

  public Optional<Company> findByName(final String name) {
    return repository.findByName(name).map(entity -> mapper.entityToModel(entity));
  }

  public void deleteById(final Integer id) {
    existsById(id);

    repository.deleteById(id);
  }

  private void existsById(final Integer id) {
    if (!repository.existsById(id)) {
      throw new CountryNotFoundException(id);
    }
  }
}
