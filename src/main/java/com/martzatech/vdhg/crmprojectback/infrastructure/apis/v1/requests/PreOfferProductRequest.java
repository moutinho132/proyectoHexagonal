package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class PreOfferProductRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = -1006121430393994685L;
    @NonNull
    private Integer productId;
    private Integer id;

    @NonNull
    private Boolean requiresPayment;

    @NonNull
    private Boolean showPrice;

    @NonNull
    private Boolean defaultBasePrice;
    private Boolean defaultVat;
    private Boolean defaultCommission;
    private Boolean priceVisible;
    private BigDecimal basePrice;
    private BigDecimal vat;
    private BigDecimal commission;
    private Boolean defaultDescription;
    private String description;
    private Boolean defaultMarketing;
    private String marketing;
    private Boolean defaultAvailabilityFrom;
    private LocalDateTime availabilityFrom;
    private  Boolean defaultAvailabilityTo;
    private LocalDateTime availabilityTo;
    private Boolean defaultPaymentDetails;
    private String  paymentDetails;
    private  Boolean defaultPaymentMethod;
    private  String paymentMethod;
    private  String paymentReference;
}
