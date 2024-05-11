package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserMobilRequestDto {
    private Integer id;
    @NotEmpty(message = CommonConstants.THIS_FIELD_IS_MANDATORY_MESSAGE)
    @Length(message = CommonConstants.MAX_LENGTH_128, max = 128)
    private final String userName;
    @NotEmpty(message = CommonConstants.THIS_FIELD_IS_MANDATORY_MESSAGE)
    @Length(message = CommonConstants.MAX_LENGTH_128, max = 128)
    private final String password;
}
