package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
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
public class DepartmentResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = -1935214658802606186L;

    private final Integer id;
    private final String name;
    private final String description;
    private final SubsidiaryResponse subsidiary;
    private final List<RoleResponse> roles;
    private final LocalDateTime creationTime;
    private final LocalDateTime modificationTime;
    private final UserResponse creationUser;
    private final UserResponse modificationUser;
    private final List<UserResponse> users;
}
