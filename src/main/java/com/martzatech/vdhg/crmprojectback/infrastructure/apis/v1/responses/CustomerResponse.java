package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.martzatech.vdhg.crmprojectback.domains.chat.models.ChatRoom;
import com.martzatech.vdhg.crmprojectback.domains.security.models.DeletedStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;

@AllArgsConstructor
@Getter
@Builder
@JsonInclude(Include.NON_EMPTY)
public class CustomerResponse implements Serializable {

  @Serial
  private static final long serialVersionUID = 5368827122315307846L;

  private final Integer id;
  private final String alias;
  private final MembershipResponse membership;
  private final CompanyResponse company;
  private final PersonResponse person;
  private final String reference;
  private final Integer loyaltyPoints;
  private final LeadResponse lead;
  private final CustomerStatusResponse status;
  private final CreationTypeResponse creationType;
  private final PaymentDetailsResponse paymentDetails;
  private final GroupAccountResponse groupAccount;
  private final BigDecimal totalSpendAmount;
  private final List<RoleResponse> roles;
  private final List<FileResponse> files;
  private final ChatRoomResponse chatRoom;
  private final UserResponse creationUser;
  private final UserResponse modificationUser;
  private final LocalDateTime creationTime;
  private final LocalDateTime modificationTime;
}
