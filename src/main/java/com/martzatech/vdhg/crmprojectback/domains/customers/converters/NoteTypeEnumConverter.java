package com.martzatech.vdhg.crmprojectback.domains.customers.converters;

import com.martzatech.vdhg.crmprojectback.domains.customers.enums.NoteTypeEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Objects;

@Converter
public class NoteTypeEnumConverter implements AttributeConverter<NoteTypeEnum, Integer> {

  @Override
  public Integer convertToDatabaseColumn(final NoteTypeEnum statusEnum) {
    return Objects.nonNull(statusEnum) ? statusEnum.getId() : null;
  }

  @Override
  public NoteTypeEnum convertToEntityAttribute(final Integer statusId) {
    return Objects.nonNull(statusId) ? NoteTypeEnum.getById(statusId) : null;
  }
}
