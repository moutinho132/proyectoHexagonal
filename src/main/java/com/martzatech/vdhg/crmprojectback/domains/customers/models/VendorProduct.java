package com.martzatech.vdhg.crmprojectback.domains.customers.models;

import com.martzatech.vdhg.crmprojectback.domains.chat.models.File;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import com.martzatech.vdhg.crmprojectback.domains.vendors.models.VendorContact;
import com.martzatech.vdhg.crmprojectback.domains.vendors.models.VendorLocation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class VendorProduct {
    private Integer vendorId;
    @With
    private final String name;
    @With
    private final String billingAddress;
    @With
    private final BigDecimal vat;
    @With
    private final BigDecimal commission;
    @With
    private final String companyReg;
    @With
    private final String financeEmail;
    @With
    private final String paymentDetail;
    @With
    private final String paymentMethod;
    @With
    private final List<VendorContact> vendorContacts;
    @With
    private final List<VendorLocation> vendorLocations;
    @With
    private final List<File> files;
    @With
    private final User creationUser;
    @With
    private final User modificationUser;
    @With
    private final LocalDateTime creationTime;
    @With
    private final LocalDateTime modificationTime;
}
