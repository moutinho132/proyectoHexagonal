package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@NoArgsConstructor
@Getter
public class VendorProductRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = -1268553652535125471L;

    private Integer vendorId;
}
