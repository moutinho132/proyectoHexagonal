package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class UserNewResponse {

    private final Integer id;
    private final String name;
    private final String surname;
    private final String email;
    private final String mobile;
    private String typeUser;
    private String token;
}
