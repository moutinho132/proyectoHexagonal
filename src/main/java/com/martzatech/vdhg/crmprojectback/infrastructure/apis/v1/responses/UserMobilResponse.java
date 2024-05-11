package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserMobilResponse {
    private Integer id;
    private final String userName;
    private final String password;
    private final UserNewResponse user;
    private final LocalDateTime creationTime;
    private final LocalDateTime lastLogin;
    private final boolean passwordUpdateRequired;
}
