package com.martzatech.vdhg.crmprojectback.domains.chat.models;

import com.martzatech.vdhg.crmprojectback.domains.commons.models.Person;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.*;
import com.martzatech.vdhg.crmprojectback.domains.security.models.DeletedStatus;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import com.martzatech.vdhg.crmprojectback.domains.vendors.models.Vendor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class File {
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
    private List<Person> persons;

    @With
    private List<Product> products;

    @With
    private List<BockingProduct> bockingProduct;

    @With
    private List<Offer> offers;

    @With
    private List<Vendor> vendors;
    @With
    private List<PreOffer> preOffers;

    @With
    private final DeletedStatus status;

    @With
    private final User removalUser;
    @With
    private final LocalDateTime creationTime;

    @With
    private final LocalDateTime removalTime;
    @With
    private final User creationUser;

}
