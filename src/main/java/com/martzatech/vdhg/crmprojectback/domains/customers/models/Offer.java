package com.martzatech.vdhg.crmprojectback.domains.customers.models;

import com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.File;
import com.martzatech.vdhg.crmprojectback.domains.customers.enums.OfferGLobalStatusEnum;
import com.martzatech.vdhg.crmprojectback.domains.customers.enums.OfferStatusEnum;
import com.martzatech.vdhg.crmprojectback.domains.security.models.DeletedStatus;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class Offer {


    private Integer id;
    @With
    private Integer number;
    @With
    private Integer version;
    @With
    private OfferStatusEnum status;
    @With
    private String textToClient;
    @With
    private OfferGLobalStatusEnum globalStatus;
    @With
    private String name;
    @With
    private PreOffer preOffer;
    @With
    private String description;
    @With
    private Customer customer;
    @With
    private List<BockingProduct> products;
    @With
    private final List<File> files;
    @With
    private String currency;
    @With
    @DecimalMin(value = "0.0", message = CommonConstants.DECIMAL_MIN_MESSAGE)
    @Digits(integer = 9, fraction = 2, message = CommonConstants.EXPECTED_DIGITS_MESSAGE)
    @DecimalMax(value = "999999999.99", message = CommonConstants.DECIMAL_MAX_MESSAGE)
    private BigDecimal subtotal;
    @With
    @DecimalMin(value = "0.0", message = CommonConstants.DECIMAL_MIN_MESSAGE)
    @Digits(integer = 9, fraction = 2, message = CommonConstants.EXPECTED_DIGITS_MESSAGE)
    @DecimalMax(value = "999999999.99", message = CommonConstants.DECIMAL_MAX_MESSAGE)
    private BigDecimal total;

    @With
    private BigDecimal totalCommission;
    @With
    private Discount discount;
    @With
    private Boolean paymentRequired;
    @With
    private String pdfUrl;
    @With
    private Boolean restricted;
    @With
    private Boolean defaultExpiration;
    @With
    private LocalDateTime expirationTime;
    @With
    private DeletedStatus deletedStatus;

    @With
    private User creationUser;

    @With
    private User modificationUser;

    @With
    private LocalDateTime creationTime;

    @With
    private LocalDateTime modificationTime;
}
