package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses;

import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
public class FileResponse {
    private final Integer id;
    private final String url;
    private final String name;
    private final String extension;
    private final String text;
    private final LocalDateTime creationTime;
    private final User creationUser;
}
