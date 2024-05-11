package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests;

import com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants;
import com.martzatech.vdhg.crmprojectback.domains.commons.models.DayOffice;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class OutOfficeRequest {
    private Integer id;

    @NotEmpty(message = CommonConstants.THIS_FIELD_IS_MANDATORY_MESSAGE)
    private String value;

    private  String start;

    private  String end;
    private String name;

    @NotEmpty(message = CommonConstants.THIS_FIELD_IS_MANDATORY_MESSAGE)
    private List<DayOffice> days;
}
