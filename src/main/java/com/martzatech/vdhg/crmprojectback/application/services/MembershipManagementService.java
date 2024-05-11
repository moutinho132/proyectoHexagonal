package com.martzatech.vdhg.crmprojectback.application.services;

import com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants;
import com.martzatech.vdhg.crmprojectback.application.exceptions.FieldIsMissingException;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Membership;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.MembershipType;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.MembershipService;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.MembershipTypeService;
import jakarta.transaction.Transactional;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
@Service
public class MembershipManagementService {

  private final MembershipService membershipService;
  private final MembershipTypeService membershipTypeService;

  /*
   * The core services
   */

  @Transactional
  public Membership save(final Membership model) {
    validations(model);
    return membershipService.save(model);
  }

  /*
   * Validators
   */

  private void validations(final Membership model) {
    validMembershipType(model.getType());
  }

  private void validMembershipType(final MembershipType membershipType) {
    if (Objects.isNull(membershipType) || Objects.isNull(membershipType.getId())) {
      throw new FieldIsMissingException(CommonConstants.TYPE);
    }
    membershipTypeService.existsById(membershipType.getId());
  }
}
