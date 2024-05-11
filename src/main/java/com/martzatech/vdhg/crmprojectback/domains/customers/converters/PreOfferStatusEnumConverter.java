package com.martzatech.vdhg.crmprojectback.domains.customers.converters;

import com.martzatech.vdhg.crmprojectback.domains.customers.enums.PreOfferStatusEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Objects;

@Converter
public class PreOfferStatusEnumConverter implements AttributeConverter<PreOfferStatusEnum, Integer> {

  @Override
  public Integer convertToDatabaseColumn(final PreOfferStatusEnum statusEnum) {
    return Objects.nonNull(statusEnum) ? statusEnum.getId() : null;
  }

  @Override
  public PreOfferStatusEnum convertToEntityAttribute(final Integer statusId) {
    return Objects.nonNull(statusId) ? PreOfferStatusEnum.getById(statusId) : null;
  }
}
