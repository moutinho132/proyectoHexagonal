package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.chat.models.ChatRoom;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.ChatRoomRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.ChatRoomResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                ChatMessageApiMapper.class
        }
)
public interface ChatRoomApiMapper {
    ChatRoom requestToModel(ChatRoomRequest request);

    List<ChatRoom> requestToModelList(List<ChatRoomRequest> list);

    ChatRoomResponse modelToResponse(ChatRoom model);

    List<ChatRoomResponse> modelsToResponseList(List<ChatRoom> list);
}
