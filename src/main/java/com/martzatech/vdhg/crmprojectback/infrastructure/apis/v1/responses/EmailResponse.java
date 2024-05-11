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
public class EmailResponse implements Serializable {

  @Serial
  private static final long serialVersionUID = 1868161920924782639L;

  private final Integer id;
  private final String value;
  private final Boolean valid;
  private final AttributeTypeResponse attributeType;
  private final LocalDateTime checkTime;
  private final LocalDateTime creationTime;
  private final LocalDateTime modificationTime;
}
