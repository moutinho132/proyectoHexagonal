package com.martzatech.vdhg.crmprojectback.domains.customers.services.mobil;

import com.martzatech.vdhg.crmprojectback.application.enums.DeletedStatusEnum;
import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.OrderNotFoundException;
import com.martzatech.vdhg.crmprojectback.application.helper.SpecificationHelper;
import com.martzatech.vdhg.crmprojectback.domains.customers.mappers.OrderMapper;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Offer;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Order;
import com.martzatech.vdhg.crmprojectback.domains.customers.repositories.OrderRepository;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.OrderMobileSpecification;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Slf4j
@Service
public class OrderMobileService {

  private OrderRepository repository;
  private OrderMapper mapper;

  public Order save(final Order model) {
    if (Objects.requireNonNullElse(model.getId(), 0) != 0) {
      existsById(model.getId());
    }

    return mapper.entityToModel(repository.save(mapper.modelToEntity(model)));
  }
  @Transactional
  public List<Order> findAll(final OrderMobileSpecification specification, final Pageable pageable) {
    log.info("FindAll Order Mobile");
    return mapper.entitiesToModelList(repository.findAll(SpecificationHelper
            .addSoftDeleteOrder(DeletedStatusEnum.ACTIVE.getId(),specification),pageable).toList());
  }
  @Transactional
  public Order findById(final Integer id) {
    return mapper.entityToModel(repository.findById(id).orElseThrow(() -> new OrderNotFoundException(id)));
  }

  public List<Order> findByOffer(final Offer offer) {
    return mapper.entitiesToModelList(repository.findByOfferId(offer.getId()));
  }

  public void deleteById(final Integer id) {
    existsById(id);

    repository.deleteById(id);
  }
  public void deleteStatus(final Integer id) {
    existsById(id);

    repository.deleteStatus(id);
  }

  public void existsById(final Integer id) {
    if (!repository.existsById(id)) {
      throw new OrderNotFoundException(id);
    }
  }

  public Long count(final OrderMobileSpecification specification) {
    return repository.count(specification);
  }
}
