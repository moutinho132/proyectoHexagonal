package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests;

import com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants;
import com.martzatech.vdhg.crmprojectback.domains.chat.enums.ChatRoomTypeEnum;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@Getter
public class ChatRoomRequest implements Serializable {

  @Serial
  private static final long serialVersionUID = -7030398914318072888L;
  private Integer id;
  @Length(message = CommonConstants.MAX_LENGTH_256, max = 256)
  private String name;

  @NotNull(message = CommonConstants.THIS_FIELD_IS_MANDATORY_MESSAGE)
  private ChatRoomTypeEnum type;

  private List<UserRequest> members;
}
