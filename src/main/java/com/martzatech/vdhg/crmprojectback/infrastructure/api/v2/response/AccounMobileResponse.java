package com.martzatech.vdhg.crmprojectback.infrastructure.api.v2.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.AssociatedResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.CustomerResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
@AllArgsConstructor
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AccounMobileResponse {
    //private final Integer id;
    private final String typeUser;
    private final CustomerResponse customer;
    private final AssociatedResponse associated;
    private final LocalDateTime creationTime;
    private final LocalDateTime modificationTime;
    private final LocalDateTime lastLogin;
}
