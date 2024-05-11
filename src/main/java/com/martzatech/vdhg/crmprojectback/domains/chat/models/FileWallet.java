package com.martzatech.vdhg.crmprojectback.domains.chat.models;

import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
public class FileWallet {
    @With
    private final Integer id;

    @With
    private final String url;

    @With
    private final String name;

    @With
    private final String extension;
    @With
    private final String text;

    @With
    private final LocalDateTime creationTime;

    @With
    private final LocalDateTime removalTime;
    @With
    private final User creationUser;

}
