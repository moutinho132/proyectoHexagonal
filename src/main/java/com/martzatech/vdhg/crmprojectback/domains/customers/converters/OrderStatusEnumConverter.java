package com.martzatech.vdhg.crmprojectback.domains.customers.converters;

import com.martzatech.vdhg.crmprojectback.domains.customers.enums.OrderStatusEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Objects;

@Converter
public class OrderStatusEnumConverter implements AttributeConverter<OrderStatusEnum, Integer> {

  @Override
  public Integer convertToDatabaseColumn(final OrderStatusEnum statusEnum) {
    return Objects.nonNull(statusEnum) ? statusEnum.getId() : null;
  }

  @Override
  public OrderStatusEnum convertToEntityAttribute(final Integer statusId) {
    return Objects.nonNull(statusId) ? OrderStatusEnum.getById(statusId) : null;
  }
}
