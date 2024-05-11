package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@NoArgsConstructor
@Getter
public class ProductLocationRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = -1258553652535125471L;
    private Integer id;
    private String address;
    private String mapUrl;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String placeId;

}
