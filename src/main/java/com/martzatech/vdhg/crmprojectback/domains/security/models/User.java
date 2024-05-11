package com.martzatech.vdhg.crmprojectback.domains.security.models;

import com.martzatech.vdhg.crmprojectback.domains.customers.models.Associated;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class User {

    private final Integer id;

    @With
    private final String title;

    @With
    private final String name;

    @With
    private final String surname;

    @With
    private final String email;

    @With
    private final String mobile;

    @With
    private final String address;

    @With
    private final String nationality;

    @With
    private final UserStatus status;

    @With
    private String typeUser;

    @With
    private final Customer customer;

    @With
    private final Associated associated;

    @With
    private final List<Role> roles;

    @With
    private final LocalDateTime creationTime;

    @With
    private final LocalDateTime modificationTime;

    @With
    private final LocalDateTime lastLogin;

    @With
    private final User creationUser;

    @With
    private final User modificationUser;
}
