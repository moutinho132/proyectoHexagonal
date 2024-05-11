package com.martzatech.vdhg.crmprojectback.domains.security.models;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserMobil {
    private Integer id;
    @With
    private UserNew user;
    @With
    private final String userName;
    @With
    private final String password;
    @With
    private final String token;
    @With
    private final LocalDateTime lastLogin;
    @With
    private final boolean passwordUpdateRequired;
    @With
    private final LocalDateTime creationTime;

}
