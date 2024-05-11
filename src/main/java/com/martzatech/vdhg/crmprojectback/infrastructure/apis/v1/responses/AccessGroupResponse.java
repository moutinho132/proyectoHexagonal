package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
@JsonInclude(Include.NON_EMPTY)
public class AccessGroupResponse implements Serializable {

  @Serial
  private static final long serialVersionUID = -7806951972642702893L;

  private final Integer id;
  private final String name;
  private final String description;
  private final List<AccessResponse> accesses;
}
