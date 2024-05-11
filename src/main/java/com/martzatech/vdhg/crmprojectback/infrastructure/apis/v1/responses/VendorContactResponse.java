package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class VendorContactResponse {
    private final String name;
    private final String role;
    private final String email;
    private final String telephone;
    private final String additional;
    private Integer id;
}
