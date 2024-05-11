package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests;

import com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import com.martzatech.vdhg.crmprojectback.domains.vendors.models.VendorContact;
import com.martzatech.vdhg.crmprojectback.domains.vendors.models.VendorLocation;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
public class VendorRequest {
    private Integer id;
    @NotEmpty(message = CommonConstants.THIS_FIELD_IS_MANDATORY_MESSAGE)
    @Length(message = CommonConstants.MAX_LENGTH_256, max = 256)
    private  String name;
    private  String billingAddress;

    @DecimalMin(value = "0.01", message = CommonConstants.THIS_FIELD_REQUEST_ERROR_FIELD)
    @DecimalMax(value = "100.00", message = CommonConstants.THIS_FIELD_REQUEST_ERROR_FIELD)
    private BigDecimal vat;
    @DecimalMin(value = "0.01", message = CommonConstants.THIS_FIELD_REQUEST_ERROR_FIELD)
    @DecimalMax(value = "100.00", message = CommonConstants.THIS_FIELD_REQUEST_ERROR_FIELD)
    private BigDecimal commission;
    private  String companyReg;
    private  String financeEmail;
    private  String paymentDetails;
    private  String paymentMethod;
    @NotNull(message = CommonConstants.THIS_FIELD_IS_MANDATORY_MESSAGE)
    private List<VendorContact> vendorContacts;
    @NotNull(message = CommonConstants.THIS_FIELD_IS_MANDATORY_MESSAGE)
    private  List<VendorLocation> vendorLocations;
    private  User creationUser;
    private  User modificationUser;
    private  LocalDateTime creationTime;
    private  LocalDateTime modificationTime;
}
