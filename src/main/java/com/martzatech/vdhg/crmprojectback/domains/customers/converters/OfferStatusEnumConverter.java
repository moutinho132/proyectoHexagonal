package com.martzatech.vdhg.crmprojectback.domains.customers.converters;

import com.martzatech.vdhg.crmprojectback.domains.customers.enums.OfferStatusEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Objects;

@Converter
public class OfferStatusEnumConverter implements AttributeConverter<OfferStatusEnum, Integer> {

  @Override
  public Integer convertToDatabaseColumn(final OfferStatusEnum statusEnum) {
    return Objects.nonNull(statusEnum) ? statusEnum.getId() : null;
  }

  @Override
  public OfferStatusEnum convertToEntityAttribute(final Integer statusId) {
    return Objects.nonNull(statusId) ? OfferStatusEnum.getById(statusId) : null;
  }
}
