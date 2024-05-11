package com.martzatech.vdhg.crmprojectback.domains.wallet.models;

import com.martzatech.vdhg.crmprojectback.domains.chat.models.FileWallet;
import com.martzatech.vdhg.crmprojectback.domains.commons.models.Person;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
public class Wallet {
    @With
    private final Integer id;
    @With
    private final Person person;
    @With
    private final FileWallet file;
    @With
    private final LocalDateTime creationTime;
}
