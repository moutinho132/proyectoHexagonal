package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.martzatech.vdhg.crmprojectback.domains.customers.enums.NoteTypeEnum;
import com.martzatech.vdhg.crmprojectback.domains.security.models.DeletedStatus;
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
public class NoteResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 3398280509990682542L;

    private final Integer id;
    private final String title;
    private final String description;
    private final NoteTypeEnum type;
    private final NoteElementResponse element;
    private final List<RoleResponse> roles;
    private final List<UserResponse> users;
    private final UserResponse creationUser;
    private final UserResponse modificationUser;
    private final LocalDateTime creationTime;
    private final LocalDateTime modificationTime;
}
