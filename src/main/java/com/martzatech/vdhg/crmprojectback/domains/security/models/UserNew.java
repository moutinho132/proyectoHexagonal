package com.martzatech.vdhg.crmprojectback.domains.security.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
public class UserNew {

    private final Integer id;
    @With
    private final String name;
    @With
    private final String surname;
    @With
    private final String email;
    @With
    private final String mobile;
    @With
    private String typeUser;
    @With
    private String token;
    @With
    private final LocalDateTime lastLogin;
}
