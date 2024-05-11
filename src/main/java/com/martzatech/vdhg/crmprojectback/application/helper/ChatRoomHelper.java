package com.martzatech.vdhg.crmprojectback.application.helper;

import com.martzatech.vdhg.crmprojectback.domains.chat.models.ChatRoom;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Customer;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class ChatRoomHelper {
    private static final String CHAT_DEFAULT_NAME_PREFIX = "chat-";

    public static ChatRoom buildChatRoom(final ChatRoom chatRoom, final User currentUser) {
        final List<User> currentUserAsList = List.of(currentUser);
        final LocalDateTime now = LocalDateTime.now();
        return chatRoom
                .withName(
                        StringUtils.isBlank(chatRoom.getName())
                                ? CHAT_DEFAULT_NAME_PREFIX + now.getNano()
                                : chatRoom.getName()
                )
                .withCustomer(Objects.nonNull(chatRoom.getCustomer()) ? Customer.builder().id(chatRoom.getCustomer().getId()).build() : null)
                .withGroupAccount(!Objects.isNull(chatRoom.getGroupAccount()) ? chatRoom.getGroupAccount() : null)
                .withMembers(
                        CollectionUtils.isEmpty(chatRoom.getMembers())
                                ? currentUserAsList
                                : Stream.concat(chatRoom.getMembers().stream(), currentUserAsList.stream()).toList())
                .withCreationUser(currentUser)
                .withCreationTime(now);
    }
}
