package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.martzatech.vdhg.crmprojectback.domains.security.models.DeletedStatus;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ChatOutOfficeResponse implements Serializable {
    private Integer id;

    @Serial
    private static final long serialVersionUID = 5948227642437564925L;
    private final String value;
    private final String start;
    private final String end;
    private final List<DayOfficeResponse> days;
    private final String name;
    private User creationUser;
    private User modificationUser;
}
