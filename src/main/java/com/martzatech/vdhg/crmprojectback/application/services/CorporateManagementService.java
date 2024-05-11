package com.martzatech.vdhg.crmprojectback.application.services;

import com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants;
import com.martzatech.vdhg.crmprojectback.application.enums.DeletedStatusEnum;
import com.martzatech.vdhg.crmprojectback.application.exceptions.BusinessRuleException;
import com.martzatech.vdhg.crmprojectback.application.exceptions.FieldIsMissingException;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Corporate;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Customer;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.CorporateService;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.CustomerService;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.CorporateSpecification;
import com.martzatech.vdhg.crmprojectback.domains.security.models.DeletedStatus;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@AllArgsConstructor
@Slf4j
@Service
public class CorporateManagementService {

  private static final String MAIN_CONTACT_SHOULD_BE_IN_CUSTOMER_LIST_MESSAGE = "Main contact must be on the customer list";

  private final CustomerService customerService;
  private final CorporateService corporateService;

  /*
   * The core services
   */

  @Transactional
  public Corporate save(final Corporate model) {
    validations(model);
    return corporateService.save(build(model));
  }

  private Corporate build(final Corporate model) {
    final Corporate byId = findById(model);
    return byId
        .withCreationTime(
            Objects.nonNull(byId.getCreationTime())
                ? byId.getCreationTime()
                : LocalDateTime.now()
        ).withCreationUser(
            Objects.nonNull(byId.getCreationUser())
                ? byId.getCreationUser()
                : User.builder().id(2).build()
        ).withModificationTime(
            Objects.isNull(byId.getId())
                ? byId.getModificationTime()
                : LocalDateTime.now()
        ).withModificationUser(
            Objects.isNull(byId.getId())
                ? byId.getModificationUser()
                : User.builder().id(3).build()
        )
            .withStatus(
                    Objects.isNull(model.getStatus())
                            ? DeletedStatus.builder().id(DeletedStatusEnum.ACTIVE.getId())
                            .name("Active").build()
                            : model.getStatus()
            );
  }

  private Corporate findById(final Corporate model) {
    if (Objects.nonNull(model.getId())) {
      return corporateService.findById(model.getId())
          .withMainContact(model.getMainContact())
          .withCustomers(model.getCustomers())
          .withName(model.getName())
          .withAlias(model.getAlias())
          .withIndustry(model.getIndustry())
          .withVat(model.getVat())
          .withEmail(model.getEmail());
    }
    return model;
  }

  public List<Corporate> findAll(final CorporateSpecification specification,
      final Integer page, final Integer size, final String direction, final String attribute) {
    final Direction directionEnum =
        Arrays.stream(Direction.values()).anyMatch(v -> v.name().equalsIgnoreCase(direction))
            ? Direction.fromString(direction)
            : Direction.DESC;
    final Sort sort = Sort.by(directionEnum, attribute);
    final Pageable pageable = PageRequest.of(page, size, sort);
    return corporateService.findAll(specification, pageable);
  }

  /*
   * Validators
   */

  private void validations(final Corporate model) {
    validMainContact(model.getMainContact());
    validCustomers(model.getCustomers());
    validMainContactInCustomerList(model);
  }

  private void validMainContactInCustomerList(final Corporate model) {
    if (model.getCustomers().stream().map(Customer::getId)
        .noneMatch(id -> id.intValue() == model.getMainContact().getId())) {
      throw new BusinessRuleException(MAIN_CONTACT_SHOULD_BE_IN_CUSTOMER_LIST_MESSAGE);
    }
  }

  private void validCustomers(final List<Customer> customers) {
    if (!CollectionUtils.isEmpty(customers)) {
      final AtomicInteger counter = new AtomicInteger(0);
      customers.forEach(customer -> {
        if (Objects.isNull(customer.getId())) {
          throw new FieldIsMissingException(String.format(CommonConstants.CUSTOMERS_FIELD, counter.get()));
        }
        customerService.existsById(customer.getId());
        counter.incrementAndGet();
      });
    }
  }

  private void validMainContact(final Customer customer) {
    if (Objects.isNull(customer) || Objects.isNull(customer.getId())) {
      throw new FieldIsMissingException(CommonConstants.MAIN_CONTACT_FIELD);
    }
    customerService.existsById(customer.getId());
  }
}
