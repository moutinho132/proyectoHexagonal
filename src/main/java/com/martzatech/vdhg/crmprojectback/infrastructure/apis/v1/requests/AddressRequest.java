package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests;

import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class AddressRequest implements Serializable {

  private Integer id;
  private CountryRequest country;
  private String province;
  private String street;
  private String complement;
  private String zipCode;
  private String city;
  private AttributeTypeRequest attributeType;
}
