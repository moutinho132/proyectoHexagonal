package com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    private String message;
    private int statusCode;

}
