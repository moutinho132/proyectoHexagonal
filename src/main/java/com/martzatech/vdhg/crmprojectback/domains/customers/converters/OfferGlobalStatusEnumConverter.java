package com.martzatech.vdhg.crmprojectback.domains.customers.converters;

import com.martzatech.vdhg.crmprojectback.domains.customers.enums.OfferGLobalStatusEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Objects;

@Converter
public class OfferGlobalStatusEnumConverter implements AttributeConverter<OfferGLobalStatusEnum, Integer> {

  @Override
  public Integer convertToDatabaseColumn(final OfferGLobalStatusEnum statusEnum) {
    return Objects.nonNull(statusEnum) ? statusEnum.getId() : null;
  }

  @Override
  public OfferGLobalStatusEnum convertToEntityAttribute(final Integer statusId) {
    return Objects.nonNull(statusId) ? OfferGLobalStatusEnum.getById(statusId) : null;
  }
}
