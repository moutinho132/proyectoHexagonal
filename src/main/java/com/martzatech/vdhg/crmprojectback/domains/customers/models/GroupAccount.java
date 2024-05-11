package com.martzatech.vdhg.crmprojectback.domains.customers.models;

import com.martzatech.vdhg.crmprojectback.domains.chat.models.ChatRoom;
import com.martzatech.vdhg.crmprojectback.domains.security.models.DeletedStatus;
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
public class GroupAccount {

    private final Integer id;

    @With
    private final String name;

    @With
    private final String email;

    @With
    private final String industry;

    @With
    private final String vat;

    @With
    private final String alias;

    @With
    private final Customer owner;
    @With
    private final ChatRoom chatRoom;

    @With
    private final List<Associated> associates;

    @With
    private final DeletedStatus status;

    @With
    private final User creationUser;

    @With
    private final User modificationUser;

    @With
    private final LocalDateTime creationTime;

    @With
    private final LocalDateTime modificationTime;
}
