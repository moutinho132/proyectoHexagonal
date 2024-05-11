package com.martzatech.vdhg.crmprojectback.domains.vendors.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;

@AllArgsConstructor
@Getter
@Builder
public class VendorContact {
    @With
    private Integer id;
    @With
    private final String name;
    @With
    private final String role;
    @With
    private final String email;
    @With
    private final String telephone;
    @With
    private final String additional;
}
