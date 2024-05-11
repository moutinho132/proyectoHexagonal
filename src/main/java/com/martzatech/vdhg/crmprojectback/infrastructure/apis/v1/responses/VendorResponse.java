package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class VendorResponse {
    private Integer id;
    private String name;
    private String billingAddress;
    private BigDecimal vat;
    private BigDecimal commission;
    private String companyReg;
    private String financeEmail;
    private String paymentDetails;
    private String paymentMethod;
    private List<VendorContact> vendorContacts;
    private List<VendorLocation> vendorLocations;
    private final List<FileResponse> files;
    private final  List<ProductResponse> products;
    private User creationUser;
    private User modificationUser;
    private LocalDateTime creationTime;
    private LocalDateTime modificationTime;
}
