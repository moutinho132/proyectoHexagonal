package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.chat.models.File;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.ChatMessageRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.ChatMessageFileResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring"
)
public interface ChatMessageFileApiMapper {

    File requestToModel(ChatMessageRequest request);

    List<File> requestToModelList(List<ChatMessageRequest> list);

    ChatMessageFileResponse modelToResponse(File model);

    List<ChatMessageFileResponse> modelsToResponseList(List<File> list);
}
