package com.martzatech.vdhg.crmprojectback.domains.customers.mappers;

import com.martzatech.vdhg.crmprojectback.domains.chat.entities.FileEntity;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.File;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.*;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.*;
import com.martzatech.vdhg.crmprojectback.domains.security.entities.UserEntity;
import com.martzatech.vdhg.crmprojectback.domains.security.mappers.UserMapper;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import com.martzatech.vdhg.crmprojectback.domains.vendors.entities.VendorEntity;
import com.martzatech.vdhg.crmprojectback.domains.vendors.models.Vendor;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.FileResponse;
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
                BockingProductMapper.class,
                UserMapper.class,
        }
)
public interface PreOfferMapper {
    @Mapping(source = "creationUser", target = "creationUser", qualifiedByName = "customUser")
    @Mapping(source = "modificationUser", target = "modificationUser", qualifiedByName = "customUser")
    @Mapping(source = "products", target = "products", qualifiedByName = "customerBockingProductMapping")
    //@Mapping(source = "discount", target = "discount", ignore = true)
    PreOffer entityToModel(PreOfferEntity entity);

    List<PreOffer> entitiesToModelList(List<PreOfferEntity> list);

    @Mapping(source = "creationUser", target = "creationUser", qualifiedByName = "customUserToEntity")
    @Mapping(source = "modificationUser", target = "modificationUser", qualifiedByName = "customUserToEntity")
    @Mapping(source = "products", target = "products", qualifiedByName = "customerBockingProductEntityMapping")
    PreOfferEntity modelToEntity(PreOffer model);

    List<PreOfferEntity> modelsToEntityList(List<PreOffer> list);

    @Named("customUser")
    static User customUser(final UserEntity entity) {
        return Objects.nonNull(entity)
                ? User
                .builder()
                .id(Objects.nonNull(entity.getId()) ? entity.getId() : null)
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

    @Named("customUserToEntity")
    static UserEntity customUserToEntity(final User user) {
        return Objects.nonNull(user)
                ? UserEntity
                .builder()
                .id(Objects.nonNull(user.getId()) ? user.getId() : null)
                .title(Objects.nonNull(user.getTitle()) ? user.getTitle() : null)
                .name(Objects.nonNull(user.getName()) ? user.getName() : null)
                .surname(Objects.nonNull(user.getSurname()) ? user.getSurname() : null)
                .mobile(Objects.nonNull(user.getMobile()) ? user.getMobile() : null)
                .nationality(Objects.nonNull(user.getNationality()) ? user.getNationality() : null)
                .address(Objects.nonNull(user.getAddress()) ? user.getAddress() : null)
                .email(Objects.nonNull(user.getEmail()) ? user.getEmail() : null)
                .creationTime(Objects.nonNull(user.getCreationTime()) ? user.getCreationTime() : null)
                .modificationTime(Objects.nonNull(user.getModificationTime()) ? user.getModificationTime() : null)
                .build()
                : null;
    }
    @AfterMapping
    default void afterMappingEntity(@MappingTarget final PreOfferEntity.PreOfferEntityBuilder builder,
                                    final PreOffer preOffer) {
        if (!CollectionUtils.isEmpty(preOffer.getProducts()) && Objects.nonNull(preOffer.getId())) {
            builder.products(
                    preOffer.getProducts().stream().map(pp ->
                                    BockingProductEntity.builder()
                                            .id(pp.getId())
                                            .basePrice(Objects.nonNull(pp) ? pp.getBasePrice() : null)
                                            .availabilityTo(Objects.nonNull(pp.getAvailabilityTo()) ? pp.getAvailabilityTo() : null)
                                            .commissionPercentage(Objects.nonNull(pp.getCommissionPercentage()) ? pp.getCommissionPercentage() : null)
                                            .defaultBasePrice(Objects.nonNull(pp.getDefaultBasePrice()) ? pp.getDefaultBasePrice() : null)
                                            .defaultCommission(Objects.nonNull(pp.getDefaultCommission()) ? pp.getDefaultCommission() : null)
                                            .defaultMarketing(Objects.nonNull(pp.getDefaultMarketing()) ? pp.getDefaultMarketing() : null)
                                            .defaultPaymentDetails(Objects.nonNull(pp.getDefaultPaymentDetails()) ? pp.getDefaultPaymentDetails() : null)
                                            .vatPercentage(Objects.nonNull(pp.getVatPercentage()) ? pp.getVatPercentage() : null)
                                            .description(Objects.nonNull(pp.getDescription()) ? pp.getDescription() : null)
                                            .marketing(Objects.nonNull(pp.getMarketing()) ? pp.getMarketing() : null)
                                            .paymentDetails(Objects.nonNull(pp.getPaymentDetails()) ? pp.getPaymentDetails() : null)
                                            .defaultVat(Objects.nonNull(pp.getDefaultVat()) ? pp.getDefaultVat() : null)
                                            .paymentReference(Objects.nonNull(pp.getPaymentReference()) ? pp.getPaymentReference() : null)
                                            .requiresPayment(Objects.nonNull(pp.getRequiresPayment()) ? pp.getRequiresPayment() : null)
                                            .showPrice(Objects.nonNull(pp.getShowPrice()) ? pp.getShowPrice() : null)
                                            .defaultDescription(Objects.nonNull(pp.getDescription()) ? pp.getDefaultDescription() : null)
                                            .availabilityFrom(Objects.nonNull(pp.getAvailabilityFrom()) ? pp.getAvailabilityFrom() : null)
                                            .defaultAvailabilityFrom(Objects.nonNull(pp.getDefaultAvailabilityFrom()) ? pp.getDefaultAvailabilityFrom() : null)
                                            .defaultPaymentMethod(Objects.nonNull(pp.getDefaultPaymentMethod()) ? pp.getDefaultPaymentMethod() : null)
                                            .paymentMethod(Objects.nonNull(pp.getPaymentMethod()) ? pp.getPaymentMethod() : null)
                                            .preOffer(PreOfferEntity.builder().id(preOffer.getId()).build())
                                            .product(Objects.isNull(pp.getProduct()) ? null : ProductEntity.builder().id(pp.getProduct().getId()).build())
                                            .build()
                            )
                            .collect(Collectors.toList())
            );
        } else {
            builder.products(new ArrayList<>());
        }
    }


    @Named("customerBockingProductMapping")
    default List<BockingProduct> customerBockingProductMapping(final List<BockingProductEntity> productEntities) {
        return !CollectionUtils.isEmpty(productEntities)
                ? productEntities.stream()
                .map(bockingProductEntity -> BockingProduct.builder()
                        .id(bockingProductEntity.getId())
                        .productId(Objects.nonNull(bockingProductEntity.getProduct()) && Objects.nonNull(bockingProductEntity.getProduct().getId()) ? bockingProductEntity.getProduct().getId() : null)
                        .productName(Objects.nonNull(bockingProductEntity.getProduct()) ? bockingProductEntity.getProduct().getName() : null)
                        .vendor(Objects.nonNull(bockingProductEntity.getProduct()) ?
                                Vendor
                                        .builder()
                                        .id(bockingProductEntity.getProduct().getVendor().getId())
                                        .name(bockingProductEntity.getProduct().getVendor().getName())
                                        .commission(bockingProductEntity.getProduct().getVendor().getCommission())
                                        .vat(bockingProductEntity.getProduct().getVendor().getVat())
                                        .build():null)
                        .basePrice(Objects.nonNull(bockingProductEntity) ? bockingProductEntity.getBasePrice() : null)
                        .totalWithCommission(Objects.nonNull(bockingProductEntity.getTotalWithCommission()) ? bockingProductEntity.getTotalWithCommission() : null)
                        .totalWithVat(Objects.nonNull(bockingProductEntity.getTotalWithVat())?bockingProductEntity.getTotalWithVat():null)
                        .availabilityTo(Objects.nonNull(bockingProductEntity.getAvailabilityTo()) ? bockingProductEntity.getAvailabilityTo() : null)
                        .commissionPercentage(Objects.nonNull(bockingProductEntity.getCommissionPercentage()) ? bockingProductEntity.getCommissionPercentage() : null)
                        .defaultBasePrice(Objects.nonNull(bockingProductEntity.getDefaultBasePrice()) ? bockingProductEntity.getDefaultBasePrice() : null)
                        .defaultCommission(Objects.nonNull(bockingProductEntity.getDefaultCommission()) ? bockingProductEntity.getDefaultCommission() : null)
                        .defaultMarketing(Objects.nonNull(bockingProductEntity.getDefaultMarketing()) ? bockingProductEntity.getDefaultMarketing() : null)
                        .defaultPaymentDetails(Objects.nonNull(bockingProductEntity.getDefaultPaymentDetails()) ? bockingProductEntity.getDefaultPaymentDetails() : null)
                        .vatPercentage(Objects.nonNull(bockingProductEntity.getVatPercentage()) ? bockingProductEntity.getVatPercentage() : null)
                        .description(Objects.nonNull(bockingProductEntity.getDescription()) ? bockingProductEntity.getDescription() : null)
                        .marketing(Objects.nonNull(bockingProductEntity.getMarketing()) ? bockingProductEntity.getMarketing() : null)
                        .paymentDetails(Objects.nonNull(bockingProductEntity.getPaymentDetails()) ? bockingProductEntity.getPaymentDetails() : null)
                        .defaultVat(Objects.nonNull(bockingProductEntity.getDefaultVat()) ? bockingProductEntity.getDefaultVat() : null)
                        .paymentReference(Objects.nonNull(bockingProductEntity.getPaymentReference()) ? bockingProductEntity.getPaymentReference() : null)
                        .requiresPayment(Objects.nonNull(bockingProductEntity.getRequiresPayment()) ? bockingProductEntity.getRequiresPayment() : null)
                        .showPrice(Objects.nonNull(bockingProductEntity.getShowPrice()) ? bockingProductEntity.getShowPrice() : null)
                        .defaultDescription(Objects.nonNull(bockingProductEntity.getDescription()) ? bockingProductEntity.getDefaultDescription() : null)
                        .availabilityFrom(Objects.nonNull(bockingProductEntity.getAvailabilityFrom()) ? bockingProductEntity.getAvailabilityFrom() : null)
                        .defaultAvailabilityFrom(Objects.nonNull(bockingProductEntity.getDefaultAvailabilityFrom()) ? bockingProductEntity.getDefaultAvailabilityFrom() : null)
                        .defaultPaymentMethod(Objects.nonNull(bockingProductEntity.getDefaultPaymentMethod()) ? bockingProductEntity.getDefaultPaymentMethod() : null)
                        .paymentMethod(Objects.nonNull(bockingProductEntity.getPaymentMethod()) ? bockingProductEntity.getPaymentMethod() : null)
                        .defaultAvailabilityTo(Objects.nonNull(bockingProductEntity.getDefaultAvailabilityTo()) ? bockingProductEntity.getDefaultAvailabilityTo() : null)
                        .preOffer(Objects.nonNull(bockingProductEntity.getPreOffer()) ? PreOffer.builder().id(bockingProductEntity.getPreOffer().getId()).build() : null)
                        .offer(Objects.nonNull(bockingProductEntity.getOffer()) ? Offer.builder().id(bockingProductEntity.getOffer().getId()).build() : null)
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
                        .files(!CollectionUtils.isEmpty(bockingProductEntity.getFiles())
                                ? bockingProductEntity.getFiles().stream()
                                .map(fileEntity -> FileResponse.builder().id(fileEntity.getId())
                                        .name(StringUtils.isNotBlank(fileEntity.getName()) ? fileEntity.getName() : null)
                                        .url(StringUtils.isNotBlank(fileEntity.getUrl()) ? fileEntity.getUrl() : null)
                                        .text(StringUtils.isNotBlank(fileEntity.getText()) ? fileEntity.getText() : null).build()).toList() : null)
                        .build()).collect(Collectors.toList()) : null;
    }

    @Named("customerBockingProductEntityMapping")
    default List<BockingProductEntity> customerBockingProductEntityMapping(final List<BockingProduct> productEntities) {
        return !CollectionUtils.isEmpty(productEntities)
                ? productEntities.stream()
                .map(bockingProductEntity -> BockingProductEntity.builder()
                        .id(bockingProductEntity.getId())
                        .basePrice(Objects.nonNull(bockingProductEntity) ? bockingProductEntity.getBasePrice() : null)
                        .totalWithCommission(Objects.nonNull(bockingProductEntity.getTotalWithCommission()) ? bockingProductEntity.getTotalWithCommission() : null)
                        //.price(Objects.nonNull(bockingProductEntity.getBasePrice()) ? bockingProductEntity. : null)
                        .availabilityTo(Objects.nonNull(bockingProductEntity.getAvailabilityTo()) ? bockingProductEntity.getAvailabilityTo() : null)
                        .commissionPercentage(Objects.nonNull(bockingProductEntity.getCommissionPercentage()) ? bockingProductEntity.getCommissionPercentage() : null)
                        .defaultBasePrice(Objects.nonNull(bockingProductEntity.getDefaultBasePrice()) ? bockingProductEntity.getDefaultBasePrice() : null)
                        .defaultCommission(Objects.nonNull(bockingProductEntity.getDefaultCommission()) ? bockingProductEntity.getDefaultCommission() : null)
                        .defaultMarketing(Objects.nonNull(bockingProductEntity.getDefaultMarketing()) ? bockingProductEntity.getDefaultMarketing() : null)
                        .defaultPaymentDetails(Objects.nonNull(bockingProductEntity.getDefaultPaymentDetails()) ? bockingProductEntity.getDefaultPaymentDetails() : null)
                        .vatPercentage(Objects.nonNull(bockingProductEntity.getVatPercentage()) ? bockingProductEntity.getVatPercentage() : null)
                        .description(Objects.nonNull(bockingProductEntity.getDescription()) ? bockingProductEntity.getDescription() : null)
                        .marketing(Objects.nonNull(bockingProductEntity.getMarketing()) ? bockingProductEntity.getMarketing() : null)
                        .paymentDetails(Objects.nonNull(bockingProductEntity.getPaymentDetails()) ? bockingProductEntity.getPaymentDetails() : null)
                        .defaultVat(Objects.nonNull(bockingProductEntity.getDefaultVat()) ? bockingProductEntity.getDefaultVat() : null)
                        .paymentReference(Objects.nonNull(bockingProductEntity.getPaymentReference()) ? bockingProductEntity.getPaymentReference() : null)
                        .requiresPayment(Objects.nonNull(bockingProductEntity.getRequiresPayment()) ? bockingProductEntity.getRequiresPayment() : null)
                        .showPrice(Objects.nonNull(bockingProductEntity.getShowPrice()) ? bockingProductEntity.getShowPrice() : null)
                        .defaultDescription(Objects.nonNull(bockingProductEntity.getDescription()) ? bockingProductEntity.getDefaultDescription() : null)
                        .availabilityFrom(Objects.nonNull(bockingProductEntity.getAvailabilityFrom()) ? bockingProductEntity.getAvailabilityFrom() : null)
                        .defaultAvailabilityFrom(Objects.nonNull(bockingProductEntity.getDefaultAvailabilityFrom()) ? bockingProductEntity.getDefaultAvailabilityFrom() : null)
                        .defaultPaymentMethod(Objects.nonNull(bockingProductEntity.getDefaultPaymentMethod()) ? bockingProductEntity.getDefaultPaymentMethod() : null)
                        .paymentMethod(Objects.nonNull(bockingProductEntity.getPaymentMethod()) ? bockingProductEntity.getPaymentMethod() : null)
                        .defaultAvailabilityTo(Objects.nonNull(bockingProductEntity.getDefaultAvailabilityTo()) ? bockingProductEntity.getDefaultAvailabilityTo() : null)
                        .preOffer(Objects.nonNull(bockingProductEntity.getPreOffer()) ? PreOfferEntity.builder().id(bockingProductEntity.getPreOffer().getId()).build() : null)
                        .offer(Objects.nonNull(bockingProductEntity.getOffer()) ? OfferEntity.builder().id(bockingProductEntity.getOffer().getId()).build() : null)
                        .product(Objects.nonNull(bockingProductEntity.getProduct()) ? ProductEntity.builder()
                                .id(bockingProductEntity.getProduct().getId())
                                .name(Objects.nonNull(bockingProductEntity.getProduct().getName()) ? bockingProductEntity.getProduct().getName() : null)
                                .description(Objects.nonNull(bockingProductEntity.getProduct().getDescription()) ? bockingProductEntity.getProduct().getDescription() : null)
                                .basePrice(Objects.nonNull(bockingProductEntity.getProduct().getBasePrice()) ? bockingProductEntity.getProduct().getBasePrice() : null)
                                .active(Objects.nonNull(bockingProductEntity.getProduct().getActive()) ? bockingProductEntity.getProduct().getActive() : null)
                                .locations(!CollectionUtils.isEmpty(bockingProductEntity.getProduct().getLocations()) ?
                                        bockingProductEntity.getProduct()
                                                .getLocations().stream()
                                                .map(productLocationEntity -> ProductLocationEntity.builder().id(productLocationEntity.getId()).build()).toList() : null)
                                .vendor(Objects.nonNull(bockingProductEntity.getProduct().getVendor()) ?
                                        VendorEntity
                                                .builder()
                                                .id(bockingProductEntity.getProduct().getVendor().getVendorId())
                                                .vat(Objects.nonNull(bockingProductEntity.getProduct().getVendor().getVat()) ? bockingProductEntity.getProduct().getVendor().getVat() : null)
                                                .commission(Objects.nonNull(bockingProductEntity.getProduct().getVendor().getCommission()) ? bockingProductEntity.getProduct().getVendor().getCommission() : null)
                                                .build() : null)

                                .availabilityFrom(Objects.nonNull(bockingProductEntity.getProduct().getAvailabilityFrom()) ? bockingProductEntity.getProduct().getAvailabilityFrom() : null)
                                .defaultVat(Objects.nonNull(bockingProductEntity.getProduct().getDefaultVat()) ? bockingProductEntity.getProduct().getDefaultVat() : null)
                                .productVat(Objects.nonNull(bockingProductEntity.getProduct().getProductVat()) ? bockingProductEntity.getProduct().getProductVat() : null)
                                .productCommission(Objects.nonNull(bockingProductEntity.getProduct().getProductCommission()) ? bockingProductEntity.getProduct().getProductCommission() : null)
                                .defaultCommission(Objects.nonNull(bockingProductEntity.getProduct().getDefaultCommission()) ? bockingProductEntity.getProduct().getDefaultCommission() : null)
                                .files(!CollectionUtils.isEmpty(bockingProductEntity.getProduct().getFiles())
                                        ? bockingProductEntity.getProduct().getFiles().stream().map(fileEntity -> FileEntity.builder()
                                                .id(fileEntity.getId()).url(fileEntity.getUrl()).build())
                                        .collect(Collectors.toList())
                                        :null)
                                .build() : null)
                        .files(!CollectionUtils.isEmpty(bockingProductEntity.getFiles())
                                ? bockingProductEntity.getFiles().stream()
                                .map(fileEntity -> FileEntity.builder().id(fileEntity.getId())
                                        .name(StringUtils.isNotBlank(fileEntity.getName()) ? fileEntity.getName() : null)
                                        .url(StringUtils.isNotBlank(fileEntity.getUrl()) ? fileEntity.getUrl() : null)
                                        .text(StringUtils.isNotBlank(fileEntity.getText()) ? fileEntity.getText() : null).build()).toList() : null)
                        .build()).collect(Collectors.toList()) : null;
    }


}
