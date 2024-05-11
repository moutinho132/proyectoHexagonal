package com.martzatech.vdhg.crmprojectback.domains.chat.models;

import com.martzatech.vdhg.crmprojectback.domains.chat.enums.ChatRoomTypeEnum;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Customer;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.GroupAccount;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class ChatRoom {

    private final Integer id;

    @With
    private final String name;

    @With
    private final ChatRoomTypeEnum type;

    @With
    private final List<User> members;

    @With
    private final ChatMessage lastMessage;

    @With
    private final ChatMessage firstMessage;

    @With
    private final Customer customer;

    @With
    private final GroupAccount groupAccount;

    @With
    private final Integer totalUnreadMessages;
    @With
    private final Boolean archive;

    @With
    private final User creationUser;

    @With
    private final LocalDateTime creationTime;
}
