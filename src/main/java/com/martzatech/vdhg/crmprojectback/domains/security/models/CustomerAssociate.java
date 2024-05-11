package com.martzatech.vdhg.crmprojectback.domains.security.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
public class CustomerAssociate {

    private final Integer id;
    @With
    private final String title;
    @With
    private final String name;
    @With
    private final String surname;
    @With
    private final String mobile;
    @With
    private final String address;
    @With
    private final String nationality;
    @With
    private final String typeUser;
    @With
    private final String email;
    @With
    private final UserStatus status;
    @With
    private final User creationUser;
    @With
    private final LocalDateTime lastLogin;
    @With
    private final LocalDateTime creationTime;
}
