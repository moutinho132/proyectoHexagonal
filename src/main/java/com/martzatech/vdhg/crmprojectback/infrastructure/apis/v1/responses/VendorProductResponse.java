package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses;

import com.martzatech.vdhg.crmprojectback.domains.chat.models.File;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import com.martzatech.vdhg.crmprojectback.domains.vendors.models.VendorContact;
import com.martzatech.vdhg.crmprojectback.domains.vendors.models.VendorLocation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class VendorProductResponse {
    private Integer vendorId;
    private final String name;
    private final String billingAddress;
    private final BigDecimal vat;
    private final BigDecimal commission;
    private final String companyReg;
    private final String financeEmail;
    private final String paymentDetail;
    private final String paymentMethod;
    private final List<VendorContact> vendorContacts;
    private final List<VendorLocation> vendorLocations;
    private final List<File> files;
    private final User creationUser;
    private final User modificationUser;
    private final LocalDateTime creationTime;
    private final LocalDateTime modificationTime;
}
