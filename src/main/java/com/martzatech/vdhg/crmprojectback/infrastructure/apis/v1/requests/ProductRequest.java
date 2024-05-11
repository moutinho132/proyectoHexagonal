package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests;

import com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
public class ProductRequest implements Serializable {

  @Serial
  private static final long serialVersionUID = -2595730886169952216L;

  private Integer id;

  @Length(message = CommonConstants.MAX_LENGTH_256, max = 256)
  @NotEmpty(message = CommonConstants.THIS_FIELD_IS_MANDATORY_MESSAGE)
  private String name;

  @Length(message = CommonConstants.MAX_LENGTH_1024, max = 1024)
  private String description;

  @NotEmpty(message = CommonConstants.THIS_FIELD_IS_MANDATORY_MESSAGE)
  @Length(message = CommonConstants.MAX_LENGTH_1024, max = 1024)
  private String marketing;
  private Boolean defaultVat;
  private BigDecimal productVat;
  private Boolean defaultCommission;
  private BigDecimal productCommission;
  private Boolean priceVisible;
  private BigDecimal basePrice;

  private CategoryRequest category;

  private List<SubCategoryRequest> subCategories;

  private SubsidiaryRequest subsidiary;

  private List<MembershipRequest> memberships;

  private List<ProductLocationRequest> locations;

  private VendorProductRequest vendor;

  private LocalDateTime availabilityFrom;

  private LocalDateTime availabilityTo;

  private Boolean visibility;
}
