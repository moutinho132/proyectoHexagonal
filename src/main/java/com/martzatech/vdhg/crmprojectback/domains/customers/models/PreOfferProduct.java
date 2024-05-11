package com.martzatech.vdhg.crmprojectback.domains.customers.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PreOfferProduct implements Serializable {

    @Serial
    private static final long serialVersionUID = -1006121230393994685L;
    private Integer id;
    private Integer productId;
    private final BigDecimal price;
    private final Boolean requiresPayment;
    private final Boolean showPrice;
    private final Boolean defaultBasePrice;
    private final BigDecimal basePrice;
    private final Boolean defaultVat;
    private final BigDecimal vat;
    private final Boolean defaultCommission;
    private final BigDecimal commission;
    private final Boolean defaultDescription;
    private final String description;
    private final Boolean defaultMarketing;
    private final String marketing;
    private final Boolean defaultAvailabilityFrom;
    private final LocalDateTime availabilityFrom;
    private final Boolean defaultAvailabilityTo;
    private final LocalDateTime availabilityTo;
    private final String paymentReference;
    private final Boolean defaultPaymentMethod;
    private final String paymentMethod;
    private final Boolean defaultPaymentDetails;
    private final String paymentDetails;
}
