package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
@Builder
@JsonInclude(Include.NON_EMPTY)
public class ProductResponse implements Serializable {

  @Serial
  private static final long serialVersionUID = 1802556101589801654L;

  private final Integer id;
  private final String name;
  private final String description;
  private final String marketing;
  private Boolean priceVisible;
  private BigDecimal basePrice;
  private final Boolean defaultVat;
  private final BigDecimal productVat;
  private final Boolean defaultCommission;
  private final BigDecimal productCommission;
  private final CategoryResponse category;
  private final List<SubCategoryResponse> subCategories;
  private final SubsidiaryResponse subsidiary;
  private final List<MembershipResponse> memberships;
  private final VendorProductResponse vendor;
  private final List<ProductLocationResponse> locations;
  private final LocalDateTime availabilityFrom;
  private final LocalDateTime availabilityTo;
  private final Boolean active;
  private final Boolean visibility;
  private final UserResponse creationUser;
  private final List<FileResponse> files;
  private final UserResponse modificationUser;
  private final LocalDateTime creationTime;
  private final LocalDateTime modificationTime;
}
