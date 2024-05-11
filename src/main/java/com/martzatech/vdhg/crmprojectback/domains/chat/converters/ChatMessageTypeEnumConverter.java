package com.martzatech.vdhg.crmprojectback.domains.chat.converters;

import com.martzatech.vdhg.crmprojectback.domains.chat.enums.ChatMessageTypeEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Objects;

@Converter
public class ChatMessageTypeEnumConverter implements AttributeConverter<ChatMessageTypeEnum, Integer> {

  @Override
  public Integer convertToDatabaseColumn(final ChatMessageTypeEnum _enum) {
    return Objects.nonNull(_enum) ? _enum.getId() : null;
  }

  @Override
  public ChatMessageTypeEnum convertToEntityAttribute(final Integer _id) {
    return Objects.nonNull(_id) ? ChatMessageTypeEnum.getById(_id) : null;
  }
}
