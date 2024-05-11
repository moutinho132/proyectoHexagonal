package com.martzatech.vdhg.crmprojectback.domains.chat.mappers;

import com.martzatech.vdhg.crmprojectback.domains.chat.entities.ChatMessageEntity;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.ChatMessage;
import com.martzatech.vdhg.crmprojectback.domains.security.mappers.CommonNamed;
import com.martzatech.vdhg.crmprojectback.domains.security.mappers.RoleMapper;
import com.martzatech.vdhg.crmprojectback.domains.security.mappers.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                CommonNamed.class,
                ChatMessageReaderMapper.class,
                RoleMapper.class,
                ChatRoomMapper.class,
                FileMapper.class,
                UserMapper.class,

        }
)
public interface ChatMessageMapper {

    @Mapping(source = "sender", target = "sender", qualifiedByName = "customUserMapping")
    @Mapping(source = "chatRoom", target = "chatRoom", qualifiedByName = "customAssociateModelMapping")
        //@Mapping(target = "chatRoom", ignore = true)
    ChatMessage entityToModel(ChatMessageEntity entity);


    @Mapping(source = "sender", target = "sender", qualifiedByName = "customUserMapping")
    @Mapping(source = "chatRoom", target = "chatRoom", qualifiedByName = "customAssociateModelMapping")
    List<ChatMessage> entitiesToModelList(List<ChatMessageEntity> list);

    @Mapping(source = "chatRoom", target = "chatRoom", qualifiedByName = "customAssociateMapping")
    ChatMessageEntity modelToEntity(ChatMessage model);

    List<ChatMessageEntity> modelsToEntityList(List<ChatMessage> list);
}
