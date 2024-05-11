package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests;

import com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class LeadRequest implements Serializable {

  @Serial
  private static final long serialVersionUID = -4166698937145101799L;

  private Integer id;
  private RegistrationTypeRequest registrationType;

  @Valid
  @NotNull(message = CommonConstants.THIS_FIELD_IS_MANDATORY_MESSAGE)
  private PersonRequest person;

  private CustomerRequest referringCustomer;

  private CompanyRequest company;

  private String reference;
}
