package com.martzatech.vdhg.crmprojectback.domains.customers.models;

import com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.File;
import com.martzatech.vdhg.crmprojectback.domains.security.models.DeletedStatus;
import com.martzatech.vdhg.crmprojectback.domains.security.models.Subsidiary;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
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
public class Product {

  private final Integer id;

  @With
  private final String name;

  @With
  private final String description;

  @With
  private final String marketing;
  @With
  private final Boolean defaultVat;

  @With
  @DecimalMin(value = "00.00", message = CommonConstants.DECIMAL_MIN_MESSAGE_DEC)
  @Digits(integer = 9, fraction = 2, message = CommonConstants.EXPECTED_DIGITS_MESSAGE)
  @DecimalMax(value = "100.00", message = CommonConstants.DECIMAL_MAX_MESSAGE_DEC)
  private final BigDecimal productVat;

  @With
  private final Boolean defaultCommission;

  @With
  @DecimalMin(value = "00.00", message = CommonConstants.DECIMAL_MIN_MESSAGE_DEC)
  @Digits(integer = 9, fraction = 2, message = CommonConstants.EXPECTED_DIGITS_MESSAGE)
  @DecimalMax(value = "100.00", message = CommonConstants.DECIMAL_MAX_MESSAGE_DEC)
  private final BigDecimal productCommission;

  @With
  private Boolean priceVisible;
  @With
  private BigDecimal basePrice;

  @With
  private final DeletedStatus status;

  @With
  private final Category category;

  @With
  private final List<SubCategory> subCategories;

  @With
  private final Subsidiary subsidiary;
  @With
  private final VendorProduct vendor;
  @With
  private final List<ProductLocation> locations;

  @With
  private final List<Membership> memberships;

  @With
  private final LocalDateTime availabilityFrom;

  @With
  private final LocalDateTime availabilityTo;

  private final Boolean active;

  @With
  private final Boolean visibility;

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
