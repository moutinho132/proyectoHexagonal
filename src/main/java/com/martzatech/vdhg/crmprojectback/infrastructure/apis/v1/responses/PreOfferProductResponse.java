package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses;

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
public class PreOfferProductResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = -1006121230393994685L;
    private Integer productId;
    private Boolean requiresPayment;
    private Boolean showPrice;
    private Boolean defaultBasePrice;
    private Boolean defaultVat;
    private double productVat;
    private boolean defaultCommission;
    private double productCommission;
    private boolean priceVisible;
    private BigDecimal basePrice;
    private double vat;
    private double commission;
    private boolean defaultDescription;
    private String description;
    private boolean defaultMarketing;
    private String marketing;
    private boolean defaultAvailabilityFrom;
    private LocalDateTime availabilityFrom;
    private boolean defaultAvailabilityTo;
    private LocalDateTime availabilityTo;
    private final Boolean defaultPaymentDetails;
    private final String paymentDetails;
}
