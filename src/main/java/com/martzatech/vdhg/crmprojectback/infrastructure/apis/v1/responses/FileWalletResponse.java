package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
public class FileWalletResponse {
    private final Integer id;
    private final String url;
    private final String name;
    private final String extension;
    private final LocalDateTime creationTime;
    private final UserWalletResponse creationUser;
}
