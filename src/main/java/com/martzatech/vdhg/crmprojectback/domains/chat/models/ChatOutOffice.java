package com.martzatech.vdhg.crmprojectback.domains.chat.models;

import com.martzatech.vdhg.crmprojectback.domains.commons.models.DayOffice;
import com.martzatech.vdhg.crmprojectback.domains.security.models.DeletedStatus;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class ChatOutOffice {

    private Integer id;

    @With
    private final String value;

    @With
    private final List<DayOffice> days;


    @With
    private final String start;

    @With
    private final String end;

    @With
    private final String name;

    @With
    private final DeletedStatus status;

    @With
    private final User creationUser;
    @With
    private final User modificationUser;
}
