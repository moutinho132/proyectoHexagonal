package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.impl;

import com.martzatech.vdhg.crmprojectback.domains.customers.entities.ProductEntity;

public class ProductMapperImpl {

    public static  ProductEntity buildProductEntity(ProductEntity entity) {
        return ProductEntity.builder()
                .id(entity.getId())
                .subCategories(entity.getSubCategories())
                .category(entity.getCategory())
                .description(entity.getDescription()).memberships(entity.getMemberships()).name(entity.getName())
                .active(entity.getActive()).availabilityFrom(entity.getAvailabilityFrom())
                .availabilityTo(entity.getAvailabilityTo())
                .creationTime(entity.getCreationTime())
                .visibility(entity.getVisibility())
                .subsidiary(entity.getSubsidiary())
                .creationUser(entity.getCreationUser())
                .modificationTime(entity.getModificationTime())
                .marketing(entity.getMarketing())
                .productVat(entity.getProductVat())
                .productCommission(entity.getProductCommission())
                .defaultCommission(entity.getDefaultCommission())
                .defaultVat(entity.getDefaultVat())
                .basePrice(entity.getBasePrice())
                .priceVisible(entity.getPriceVisible())
                .vendor(entity.getVendor())
                .locations(entity.getLocations())
                .status(entity.getStatus())
                .modificationUser(entity.getModificationUser())
                .build();
    }
}
