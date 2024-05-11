package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import com.martzatech.vdhg.crmprojectback.domains.security.models.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
@JsonInclude(Include.NON_EMPTY)
public class CustomerAssociateResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 5368827122315307846L;

    private final Integer id;
    private final String title;
    private final String name;
    private final String surname;
    private final String mobile;
    private final String address;
    private final String nationality;
    private final String typeUser;
    private final String email;
    private final UserStatus status;
    private final User creationUser;
    private final LocalDateTime lastLogin;
    private final LocalDateTime creationTime;
}
