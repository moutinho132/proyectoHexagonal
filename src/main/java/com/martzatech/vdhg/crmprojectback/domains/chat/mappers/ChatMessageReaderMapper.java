package com.martzatech.vdhg.crmprojectback.domains.chat.mappers;

import com.martzatech.vdhg.crmprojectback.domains.chat.entities.ChatMessageReaderEntity;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.ChatMessageReader;
import com.martzatech.vdhg.crmprojectback.domains.security.mappers.CommonNamed;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
    componentModel = "spring",
    uses = {
        CommonNamed.class,
    }
)
public interface ChatMessageReaderMapper {

  @Mapping(source = "reader", target = "reader", qualifiedByName = "customUserMapping")

  @Mapping(source = "message", target = "message", qualifiedByName = "customMessageEntityMapping")
  ChatMessageReader entityToModel(ChatMessageReaderEntity entity);

  List<ChatMessageReader> entitiesToModelList(List<ChatMessageReaderEntity> list);

  @Mapping(source = "reader", target = "reader", qualifiedByName = "customUserModelMapping")
  @Mapping(source = "message", target = "message", qualifiedByName = "customMessageModelMapping")
  ChatMessageReaderEntity modelToEntity(ChatMessageReader model);

  List<ChatMessageReaderEntity> modelsToEntityList(List<ChatMessageReader> list);
}
