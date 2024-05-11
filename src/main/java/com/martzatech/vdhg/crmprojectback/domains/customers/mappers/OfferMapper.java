package com.martzatech.vdhg.crmprojectback.domains.customers.mappers;

import com.martzatech.vdhg.crmprojectback.domains.chat.entities.FileEntity;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.File;
import com.martzatech.vdhg.crmprojectback.domains.commons.models.*;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.*;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.*;
import com.martzatech.vdhg.crmprojectback.domains.security.entities.UserEntity;
import com.martzatech.vdhg.crmprojectback.domains.security.mappers.UserMapper;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import com.martzatech.vdhg.crmprojectback.domains.vendors.models.Vendor;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.FileResponse;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.*;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
        uses = {
                DiscountMapper.class,
                PreOfferMapper.class,
                CustomerMapper.class,
                BockingProductMapper.class,
                UserMapper.class,
        }
)
public interface OfferMapper {


    @Mapping(source = "creationUser", target = "creationUser", qualifiedByName = "customUsers")
    @Mapping(source = "modificationUser", target = "modificationUser",qualifiedByName = "customUsers")
    @Mapping(source = "customer", target = "customer", qualifiedByName = "customerMapping")
    @Mapping(source = "products", target = "products", qualifiedByName = "customerBackingsProductMapping")
    Offer entityToModel(OfferEntity entity);

    @Transactional
    List<Offer> entitiesToModelList(List<OfferEntity> list);

    //@Mapping(target = "products", ignore = true)
    @Mapping(source = "creationUser", target = "creationUser", qualifiedByName = "customUserToEntity")
    @Mapping(source = "modificationUser", target = "modificationUser", ignore = true)
    @Mapping(source = "products", target = "products", qualifiedByName = "customerBockingProductEntityMapping")
    OfferEntity modelToEntity(Offer model);

    List<OfferEntity> modelsToEntityList(List<Offer> list);

    @Named("customerMapping")
    default Customer customerMapping(final CustomerEntity entity) {
        return Objects.nonNull(entity)
                ? Customer.builder()
                .id(entity.getId())
                .creationTime(Objects.nonNull(entity.getCreationTime()) ? entity.getCreationTime() : null)
                .loyaltyPoints(Objects.nonNull(entity.getLoyaltyPoints()) ? entity.getLoyaltyPoints() : null)
                .paymentDetails(Objects.nonNull(entity.getPaymentDetails()) ?
                        PaymentDetails.builder().id(entity.getPaymentDetails().getId()).build() : null)
                .creationType(Objects.nonNull(entity.getCreationType()) ? CreationType.builder().id(entity.getCreationType().getId()).build() : null)
                .creationUser(Objects.nonNull(entity.getCreationUser()) ? User.builder().id(entity.getCreationUser().getId()).build() : null)
                .modificationTime(Objects.nonNull(entity.getModificationTime()) ? entity.getModificationTime() : null)
                .modificationUser(Objects.nonNull(entity.getModificationUser()) ? User.builder().id(entity.getModificationUser().getId()).build() : null)
                .person(Objects.nonNull(entity.getPerson()) ? Person.builder()
                        .id(entity.getPerson().getId())
                        .title(Objects.nonNull(entity.getPerson().getTitle())? PersonTitle.builder()
                                .id(entity.getPerson().getTitle().getId()).name(entity.getPerson().getTitle().getName()).build():null)
                        .name(entity.getPerson().getName())
                        .surname(entity.getPerson().getSurname())
                        .identityDocuments(!CollectionUtils.isEmpty(entity.getPerson().getIdentityDocuments()) ?
                                entity.getPerson().getIdentityDocuments()
                                        .stream()
                                        .map(identityDocumentEntity -> IdentityDocument
                                                .builder().id(identityDocumentEntity.getId())
                                                .build())
                                        .collect(Collectors.toList()) : null)
                        .addresses(!CollectionUtils.isEmpty(entity.getPerson().getAddresses()) ? entity.getPerson().getAddresses()
                                .stream().map(addressEntity -> Address.builder().id(addressEntity.getId())
                                        .province(Objects.nonNull(addressEntity.getProvince()) ? addressEntity.getProvince() : null)
                                        .street(Objects.nonNull(addressEntity.getStreet()) ? addressEntity.getStreet() : null)
                                        .complement(Objects.nonNull(addressEntity.getComplement()) ? addressEntity.getComplement() : null)
                                        .zipCode(Objects.nonNull(addressEntity.getZipCode()) ? addressEntity.getZipCode() : null)
                                        .modificationTime(Objects.nonNull(addressEntity.getModificationTime()) ? addressEntity.getModificationTime() : null)
                                        .country(Objects.nonNull(addressEntity.getCountry()) ?
                                                Country.builder().id(addressEntity.getCountry().getId())
                                                        .isoCode(addressEntity.getCountry().getIsoCode())
                                                        .name(addressEntity.getCountry().getName())
                                                        .build() : null)
                                        .attributeType(Objects.nonNull(addressEntity.getAttributeType()) ?
                                                AttributeType.builder().id(addressEntity.getAttributeType().getId())
                                                        .name(addressEntity.getAttributeType().getName()).build() : null)
                                        .city(Objects.nonNull(addressEntity.getCity()) ? addressEntity.getCity() : null)
                                        .creationTime(Objects.nonNull(addressEntity.getCreationTime()) ? addressEntity.getCreationTime() : null)
                                        .build()).collect(Collectors.toList()) : null)
                        .build() : null)
                .membership(Objects.nonNull(entity.getMembership()) ? Membership.builder()
                        .id(entity.getMembership().getId())
                        .priority(entity.getMembership().getPriority()).build() : null)
                .build() : null;
    }
    @Named("customerBackingsProductMapping")
    default List<BockingProduct> customerBackingsProductMapping(final List<BockingProductEntity> productEntities) {
        return !CollectionUtils.isEmpty(productEntities)
                ? productEntities.stream()
                .map(bockingProductEntity -> BockingProduct.builder()
                        .id(bockingProductEntity.getId())
                        .product(Objects.nonNull(bockingProductEntity.getProduct()) ? Product.builder()
                                .id(bockingProductEntity.getProduct().getId())
                                .name(Objects.nonNull(bockingProductEntity.getProduct().getName()) ? bockingProductEntity.getProduct().getName() : null)
                                .description(Objects.nonNull(bockingProductEntity.getProduct().getDescription()) ? bockingProductEntity.getProduct().getDescription() : null)
                                .basePrice(Objects.nonNull(bockingProductEntity.getProduct().getBasePrice()) ? bockingProductEntity.getProduct().getBasePrice() : null)
                                .active(Objects.nonNull(bockingProductEntity.getProduct().getActive()) ? bockingProductEntity.getProduct().getActive() : null)
                                .marketing(Objects.nonNull(bockingProductEntity.getProduct().getMarketing())?bockingProductEntity.getProduct().getMarketing():null)
                                .locations(!CollectionUtils.isEmpty(bockingProductEntity.getProduct().getLocations()) ?
                                        bockingProductEntity.getProduct()
                                                .getLocations().stream()
                                                .map(productLocationEntity -> ProductLocation.builder().id(productLocationEntity.getId()).build()).toList() : null)
                                .vendor(Objects.nonNull(bockingProductEntity.getProduct().getVendor()) ?
                                        VendorProduct
                                                .builder()
                                                .vendorId(bockingProductEntity.getProduct().getVendor().getId())
                                                .name(bockingProductEntity.getProduct().getVendor().getName())
                                                .vat(Objects.nonNull(bockingProductEntity.getProduct().getVendor().getVat()) ? bockingProductEntity.getProduct().getVendor().getVat() : null)
                                                .commission(Objects.nonNull(bockingProductEntity.getProduct().getVendor().getCommission()) ? bockingProductEntity.getProduct().getVendor().getCommission() : null)
                                                .build() : null)

                                .availabilityFrom(Objects.nonNull(bockingProductEntity.getProduct().getAvailabilityFrom()) ? bockingProductEntity.getProduct().getAvailabilityFrom() : null)
                                .availabilityTo(Objects.nonNull(bockingProductEntity.getProduct().getAvailabilityTo()) ? bockingProductEntity.getProduct().getAvailabilityTo() : null)
                                .defaultVat(Objects.nonNull(bockingProductEntity.getProduct().getDefaultVat()) ? bockingProductEntity.getProduct().getDefaultVat() : null)
                                .productVat(Objects.nonNull(bockingProductEntity.getProduct().getProductVat()) ? bockingProductEntity.getProduct().getProductVat() : null)
                                .productCommission(Objects.nonNull(bockingProductEntity.getProduct().getProductCommission()) ? bockingProductEntity.getProduct().getProductCommission() : null)
                                .defaultCommission(Objects.nonNull(bockingProductEntity.getProduct().getDefaultCommission()) ? bockingProductEntity.getProduct().getDefaultCommission() : null)
                                .files(!CollectionUtils.isEmpty(bockingProductEntity.getProduct().getFiles())
                                        ? bockingProductEntity.getProduct().getFiles().stream().map(fileEntity -> File.builder()
                                                .id(fileEntity.getId()).url(fileEntity.getUrl()).build())
                                        .collect(Collectors.toList())
                                        :null)
                                .build() : null)
                        .productId(Objects.nonNull(bockingProductEntity.getProduct()) && Objects.nonNull(bockingProductEntity.getProduct().getId()) ? bockingProductEntity.getProduct().getId() : null)
                        .productName(Objects.nonNull(bockingProductEntity.getProduct()) ? bockingProductEntity.getProduct().getName() : null)
                        .vendor( Objects.nonNull(bockingProductEntity.getProduct()) ?
                                Vendor
                                        .builder()
                                        .id(bockingProductEntity.getProduct().getVendor().getId())
                                        .name(bockingProductEntity.getProduct().getVendor().getName())
                                        .commission(bockingProductEntity.getProduct().getVendor().getCommission())
                                        .vat(bockingProductEntity.getProduct().getVendor().getVat())
                                        .build():null)

                        .basePrice(Objects.nonNull(bockingProductEntity) ? bockingProductEntity.getBasePrice() : null)
                        .totalWithCommission(Objects.nonNull(bockingProductEntity.getTotalWithCommission()) ? bockingProductEntity.getTotalWithCommission() : null)
                        .totalWithVat(Objects.nonNull(bockingProductEntity.getTotalWithVat()) ? bockingProductEntity.getTotalWithVat() : null)
                        .availabilityTo(Objects.nonNull(bockingProductEntity.getAvailabilityTo()) ? bockingProductEntity.getAvailabilityTo() : null)
                        .commissionPercentage(Objects.nonNull(bockingProductEntity.getCommissionPercentage()) ? bockingProductEntity.getCommissionPercentage() : null)
                        .commissionValue(Objects.nonNull(bockingProductEntity.getCommissionValue()) ? bockingProductEntity.getCommissionValue() : null)
                        .defaultBasePrice(Objects.nonNull(bockingProductEntity.getDefaultBasePrice()) ? bockingProductEntity.getDefaultBasePrice() : null)
                        .defaultCommission(Objects.nonNull(bockingProductEntity.getDefaultCommission()) ? bockingProductEntity.getDefaultCommission() : null)
                        .defaultMarketing(Objects.nonNull(bockingProductEntity.getDefaultMarketing()) ? bockingProductEntity.getDefaultMarketing() : null)
                        .defaultPaymentDetails(Objects.nonNull(bockingProductEntity.getDefaultPaymentDetails()) ? bockingProductEntity.getDefaultPaymentDetails() : null)
                        .vatPercentage(Objects.nonNull(bockingProductEntity.getVatPercentage()) ? bockingProductEntity.getVatPercentage() : null)
                        .defaultDescription(Objects.nonNull(bockingProductEntity.getDefaultDescription())?bockingProductEntity.getDefaultDescription():null)
                        .description(Objects.nonNull(bockingProductEntity.getDescription()) ? bockingProductEntity.getDescription() : null)
                        .marketing(Objects.nonNull(bockingProductEntity.getMarketing()) ? bockingProductEntity.getMarketing() : null)
                        .paymentDetails(Objects.nonNull(bockingProductEntity.getPaymentDetails()) ? bockingProductEntity.getPaymentDetails() : null)
                        .defaultVat(Objects.nonNull(bockingProductEntity.getDefaultVat()) ? bockingProductEntity.getDefaultVat() : null)
                        .paymentReference(Objects.nonNull(bockingProductEntity.getPaymentReference()) ? bockingProductEntity.getPaymentReference() : null)
                        .requiresPayment(Objects.nonNull(bockingProductEntity.getRequiresPayment()) ? bockingProductEntity.getRequiresPayment() : null)
                        .showPrice(Objects.nonNull(bockingProductEntity.getShowPrice()) ? bockingProductEntity.getShowPrice() : null)
                        .availabilityFrom(Objects.nonNull(bockingProductEntity.getAvailabilityFrom()) ? bockingProductEntity.getAvailabilityFrom() : null)
                        .defaultAvailabilityFrom(Objects.nonNull(bockingProductEntity.getDefaultAvailabilityFrom()) ? bockingProductEntity.getDefaultAvailabilityFrom() : null)
                        .defaultPaymentMethod(Objects.nonNull(bockingProductEntity.getDefaultPaymentMethod()) ? bockingProductEntity.getDefaultPaymentMethod() : null)
                        .paymentMethod(Objects.nonNull(bockingProductEntity.getPaymentMethod()) ? bockingProductEntity.getPaymentMethod() : null)
                        .defaultAvailabilityTo(Objects.nonNull(bockingProductEntity.getDefaultAvailabilityTo()) ? bockingProductEntity.getDefaultAvailabilityTo() : null)
                        .preOffer(Objects.nonNull(bockingProductEntity.getPreOffer()) ? PreOffer.builder().id(bockingProductEntity.getPreOffer().getId()).build() : null)
                        .offer(Objects.nonNull(bockingProductEntity.getOffer()) ? Offer.builder().id(bockingProductEntity.getOffer().getId()).build() : null)
                        .files(!CollectionUtils.isEmpty(bockingProductEntity.getFiles())
                                ? bockingProductEntity.getFiles().stream()
                                .map(fileEntity -> FileResponse.builder().id(fileEntity.getId())
                                        .name(StringUtils.isNotBlank(fileEntity.getName())?fileEntity.getName():null)
                                        .url(StringUtils.isNotBlank(fileEntity.getUrl())?fileEntity.getUrl():null)
                                        .text(StringUtils.isNotBlank(fileEntity.getText())?fileEntity.getText():null).build()).toList():null)
                        .build()).collect(Collectors.toList()) : null;
    }

    @AfterMapping
    default void afterMappingEntity(@MappingTarget final OfferEntity.OfferEntityBuilder builder,
                                    final Offer offer) {
        if (!CollectionUtils.isEmpty(offer.getProducts()) && Objects.nonNull(offer.getId())) {
            builder.products(
                    offer.getProducts().stream().map(pp ->
                                    BockingProductEntity.builder()
                                            .id(Objects.nonNull(pp.getId()) ? pp.getId() : null)
                                            .basePrice(Objects.nonNull(pp) ? pp.getBasePrice() : null)
                                            //.price(Objects.nonNull(pp.getBasePrice()) ? pp.getPrice() : null)
                                            .availabilityTo(Objects.nonNull(pp.getAvailabilityTo()) ? pp.getAvailabilityTo() : null)
                                            .commissionPercentage(Objects.nonNull(pp.getCommissionPercentage()) ? pp.getCommissionPercentage() : null)
                                            .defaultBasePrice(Objects.nonNull(pp.getDefaultBasePrice()) ? pp.getDefaultBasePrice() : null)
                                            .defaultCommission(Objects.nonNull(pp.getDefaultCommission()) ? pp.getDefaultCommission() : null)
                                            .defaultMarketing(Objects.nonNull(pp.getDefaultMarketing()) ? pp.getDefaultMarketing() : null)
                                            .defaultPaymentDetails(Objects.nonNull(pp.getDefaultPaymentDetails()) ? pp.getDefaultPaymentDetails() : null)
                                            .vatPercentage(Objects.nonNull(pp.getVatPercentage()) ? pp.getVatPercentage() : null)
                                            .defaultDescription(Objects.nonNull(pp.getDefaultDescription())?pp.getDefaultDescription():null)
                                            .description(Objects.nonNull(pp.getDescription()) ? pp.getDescription() : null)
                                            .marketing(Objects.nonNull(pp.getMarketing()) ? pp.getMarketing() : null)
                                            .paymentDetails(Objects.nonNull(pp.getPaymentDetails()) ? pp.getPaymentDetails() : null)
                                            .defaultVat(Objects.nonNull(pp.getDefaultVat()) ? pp.getDefaultVat() : null)
                                            .paymentReference(Objects.nonNull(pp.getPaymentReference()) ? pp.getPaymentReference() : null)
                                            .requiresPayment(Objects.nonNull(pp.getRequiresPayment()) ? pp.getRequiresPayment() : null)
                                            .showPrice(Objects.nonNull(pp.getShowPrice()) ? pp.getShowPrice() : null)
                                            .availabilityFrom(Objects.nonNull(pp.getAvailabilityFrom()) ? pp.getAvailabilityFrom() : null)
                                            .defaultAvailabilityFrom(Objects.nonNull(pp.getDefaultAvailabilityFrom()) ? pp.getDefaultAvailabilityFrom() : null)
                                            .defaultPaymentMethod(Objects.nonNull(pp.getDefaultPaymentMethod()) ? pp.getDefaultPaymentMethod() : null)
                                            .paymentMethod(Objects.nonNull(pp.getPaymentMethod()) ? pp.getPaymentMethod() : null)
                                            .preOffer(Objects.nonNull(pp.getOffer()) ? PreOfferEntity.builder().id(pp.getOffer().getId()).build() : null)
                                            .product(Objects.isNull(pp.getProduct()) ? null : ProductEntity.builder().id(pp.getProduct().getId()).build())
                                            .build()
                            )
                            .collect(Collectors.toList())
            );
        } else {
            builder.products(new ArrayList<>());
        }
    }


    @Named("customUsers")
    static User customUsers(final UserEntity entity) {
        return Objects.nonNull(entity)
                ? User
                .builder()
                .id(entity.getId())
                .title(Objects.nonNull(entity.getTitle()) ? entity.getTitle() : null)
                .name(Objects.nonNull(entity.getName()) ? entity.getName() : null)
                .surname(Objects.nonNull(entity.getSurname()) ? entity.getSurname() : null)
                .mobile(Objects.nonNull(entity.getMobile()) ? entity.getMobile() : null)
                .nationality(Objects.nonNull(entity.getNationality()) ? entity.getNationality() : null)
                .address(Objects.nonNull(entity.getAddress()) ? entity.getAddress() : null)
                .email(Objects.nonNull(entity.getEmail()) ? entity.getEmail() : null)
                .creationTime(Objects.nonNull(entity.getCreationTime()) ? entity.getCreationTime() : null)
                .modificationTime(Objects.nonNull(entity.getModificationTime()) ? entity.getModificationTime() : null)
                .build()
                : null;
    }

    @Named("fileEntityListToFileList")
    static List<File> fileEntityListToFileList(List<FileEntity> list) {
        return Objects.nonNull(list) ? list.stream()
                .map(fileEntity -> File
                        .builder()
                        .id(fileEntity.getId())
                        .creationUser(User.builder().id(fileEntity.getCreationUser().getId()).build())
                        .creationTime(Objects.nonNull(fileEntity.getCreationTime()) ? fileEntity.getCreationTime() : null)
                        .extension(fileEntity.getExtension())
                        .name(fileEntity.getName())
                        .url(fileEntity.getUrl())
                        .build()).collect(Collectors.toList()) : null;
    }

    @Named("customerListFileMappings")
    static List<File> customerListFileMappings(List<FileEntity> entities) {
        return Objects.nonNull(entities) ? entities.stream()
                .map(fileEntity -> File
                        .builder()
                        .id(fileEntity.getId())
                        .creationUser(Objects.nonNull(fileEntity.getCreationUser())? User.builder().id(fileEntity.getCreationUser().getId()).build():null)
                        .creationTime(Objects.nonNull(fileEntity.getCreationTime()) ? fileEntity.getCreationTime() : null)
                        .extension(fileEntity.getExtension())
                        .name(fileEntity.getName())
                        .url(fileEntity.getUrl())
                        .build()).collect(Collectors.toList()) : null;
    }
}
