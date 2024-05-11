package com.martzatech.vdhg.crmprojectback.infrastructure.api.v2.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.martzatech.vdhg.crmprojectback.domains.customers.enums.OfferStatusEnum;
import com.martzatech.vdhg.crmprojectback.domains.customers.enums.PreOfferStatusEnum;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.DiscountResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.ProductPreOfferPriceBokingsResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@AllArgsConstructor
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PreOfferMobileResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 2632452407576927120L;

    private final Integer id;
    private final Integer number;
    private final Integer version;
    private final OfferStatusEnum status;
    private final PreOfferStatusEnum globalStatus;
    private final String textToClient;
    private final String name;
    private final String description;
    private final List<ProductPreOfferPriceBokingsResponse> products;
    private final String currency;
    private final BigDecimal subtotal;
    private final BigDecimal total;
    private final DiscountResponse discount;
    private final Boolean paymentRequired;
    private Boolean defaultExpiration;
    private final LocalDateTime expirationTime;
    private final Boolean active;
    private final String pdfUrl;
    private final UserResponse creationUser;
    private final UserResponse modificationUser;
    private final LocalDateTime creationTime;
    private final LocalDateTime modificationTime;
}
