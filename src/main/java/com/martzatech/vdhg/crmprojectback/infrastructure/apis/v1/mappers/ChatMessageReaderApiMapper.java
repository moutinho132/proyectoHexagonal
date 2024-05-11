package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.chat.models.ChatMessageReader;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.ChatMessageReaderResponse;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring"
)
public interface ChatMessageReaderApiMapper {

  ChatMessageReaderResponse modelToResponse(ChatMessageReader model);

  List<ChatMessageReaderResponse> modelsToResponseList(List<ChatMessageReader> list);
}
