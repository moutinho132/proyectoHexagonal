package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.martzatech.vdhg.crmprojectback.domains.chat.enums.ChatRoomTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
@Builder
@JsonInclude(Include.NON_EMPTY)
public class ChatRoomResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = -7266418363513072953L;

    private final Integer id;
    private final String name;
    private final ChatRoomTypeEnum type;
    private final GroupAccountResponse groupAccount;
    private final List<UserResponse> members;
    private final ChatMessageResponse lastMessage;
    private final Integer totalUnreadMessages;
    private final boolean archive;
    private final UserResponse creationUser;
    private final LocalDateTime creationTime;
}
