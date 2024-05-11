package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.chat.models.ChatMessage;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.ChatMessageRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.ChatMessageResponse;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = {
        ChatMessageReaderApiMapper.class
    }
)
public interface ChatMessageApiMapper {

  ChatMessage requestToModel(ChatMessageRequest request);

  List<ChatMessage> requestToModelList(List<ChatMessageRequest> list);

  ChatMessageResponse modelToResponse(ChatMessage model);

  List<ChatMessageResponse> modelsToResponseList(List<ChatMessage> list);
}
