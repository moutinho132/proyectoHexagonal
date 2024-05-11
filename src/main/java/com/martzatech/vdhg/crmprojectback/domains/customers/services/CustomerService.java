package com.martzatech.vdhg.crmprojectback.domains.customers.services;

import com.martzatech.vdhg.crmprojectback.application.enums.DeletedStatusEnum;
import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.CustomerNotFoundException;
import com.martzatech.vdhg.crmprojectback.application.services.PersonFieldsBlockerService;
import com.martzatech.vdhg.crmprojectback.domains.chat.repositories.ChatMessageFileRepository;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.CustomerEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.mappers.CustomerMapper;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Customer;
import com.martzatech.vdhg.crmprojectback.domains.customers.repositories.CustomerRepository;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.CustomerSpecification;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.FileSpecification;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.martzatech.vdhg.crmprojectback.application.helper.SpecificationHelper.addSoftDeleteCustomer;

@AllArgsConstructor
@Slf4j
@Service
public class CustomerService {

  private CustomerRepository repository;
  private CustomerMapper mapper;
  private PersonFieldsBlockerService personFieldsBlockerService;
  private ChatMessageFileRepository fileRepository;

  public Customer save(final Customer model) {
    if (Objects.requireNonNullElse(model.getId(), 0) != 0) {
      existsById(model.getId());
    }

    return mapper.entityToModel(repository.save(mapper.modelToEntity(model)));
  }

  public List<Customer> findAll(final CustomerSpecification specification, final Pageable pageable) {
    return (List<Customer>) personFieldsBlockerService.block(
        mapper.entitiesToModelList(repository
                .findAll(addSoftDeleteCustomer(DeletedStatusEnum.ACTIVE.getId(),specification), pageable)
                .toList())
    );
  }

  public Long count(final CustomerSpecification specification) {
    return repository.count(specification);
  }

  public Long countFile(final FileSpecification specification) {
    return fileRepository.count(specification);
  }
  public Customer findById(final Integer id) {
    return (Customer) personFieldsBlockerService.block(
        mapper.entityToModel(repository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id)))
    );
  }

  public void deleteById(final Integer id) {
    existsById(id);

    repository.deleteById(id);
  }
  public void deleteByStatus(final Integer id) {
    existsById(id);

    repository.deleteByStatus(id);
  }
  public void existsById(final Integer id) {
    if (!repository.existsById(id)) {
      throw new CustomerNotFoundException(id);
    }
  }
  public boolean existsCustomerById(final Integer id) {
    return repository.existsById(id);
  }
  public Customer findByLeadId(final Integer leadId) {
    final Optional<CustomerEntity> byLeadId = repository.findByLeadId(leadId);
    return byLeadId.map(customerEntity -> mapper.entityToModel(customerEntity)).orElse(null);
  }

  public Customer findByEmail(final String email) {
    final List<CustomerEntity> byEmail = repository.findByEmail(email);
    if (CollectionUtils.isEmpty(byEmail)) {
      return null;
    }
    return mapper.entityToModel(byEmail.get(0));
  }
}
