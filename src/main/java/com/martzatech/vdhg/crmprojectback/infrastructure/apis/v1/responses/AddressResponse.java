package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
@JsonInclude(Include.NON_EMPTY)
public class AddressResponse implements Serializable {

  @Serial
  private static final long serialVersionUID = -1948760494704000227L;

  private final Integer id;
  private final CountryResponse country;
  private final String province;
  private final String street;
  private final String complement;
  private final String zipCode;
  private final String city;
  private final AttributeTypeResponse attributeType;
  private final LocalDateTime creationTime;
  private final LocalDateTime modificationTime;
}
