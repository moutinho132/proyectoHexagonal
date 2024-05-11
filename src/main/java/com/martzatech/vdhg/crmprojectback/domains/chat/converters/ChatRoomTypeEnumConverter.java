package com.martzatech.vdhg.crmprojectback.domains.chat.converters;

import com.martzatech.vdhg.crmprojectback.domains.chat.enums.ChatRoomTypeEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Objects;

@Converter
public class ChatRoomTypeEnumConverter implements AttributeConverter<ChatRoomTypeEnum, Integer> {

  @Override
  public Integer convertToDatabaseColumn(final ChatRoomTypeEnum _enum) {
    return Objects.nonNull(_enum) ? _enum.getId() : null;
  }

  @Override
  public ChatRoomTypeEnum convertToEntityAttribute(final Integer _id) {
    return Objects.nonNull(_id) ? ChatRoomTypeEnum.getById(_id) : null;
  }
}
