package com.martzatech.vdhg.crmprojectback.domains.vendors.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;

@AllArgsConstructor
@Getter
@Builder
public class VendorLocation {
    private Integer id;
    @With
    private final String name;
    @With
    private final String address;
}
