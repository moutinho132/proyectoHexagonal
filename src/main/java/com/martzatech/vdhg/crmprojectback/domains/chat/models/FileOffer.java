package com.martzatech.vdhg.crmprojectback.domains.chat.models;

import com.martzatech.vdhg.crmprojectback.domains.commons.models.Person;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Offer;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Order;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Product;
import com.martzatech.vdhg.crmprojectback.domains.security.models.DeletedStatus;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class FileOffer {
    @With
    private final Integer id;

    @With
    private final String url;

    @With
    private final String name;

    @With
    private final String extension;

    @With
    private final LocalDateTime creationTime;
    @With
    private final User creationUser;

}
