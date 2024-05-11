package com.martzatech.vdhg.crmprojectback.domains.customers.mappers;

import com.martzatech.vdhg.crmprojectback.domains.chat.entities.FileEntity;
import com.martzatech.vdhg.crmprojectback.domains.chat.mappers.FileMapper;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.BockingProductEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.OfferEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.PreOfferEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.ProductEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.*;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import com.martzatech.vdhg.crmprojectback.domains.vendors.entities.VendorEntity;
import com.martzatech.vdhg.crmprojectback.domains.vendors.mapper.VendorMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.FileResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
        uses = {
                OfferMapper.class,
                PreOfferMapper.class,
                FileMapper.class,
                VendorMapper.class,
                ProductMapper.class,
        }
)
public interface BockingProductMapper {

    @Mapping(target = "preOffer", source = "preOffer", qualifiedByName = "customPreOffer")
    @Mapping(target = "offer", source = "offer", qualifiedByName = "customOffer")
    @Mapping(target = "files", source = "files", qualifiedByName = "customerFileMappings")
    @Mapping(target = "product", source = "product", qualifiedByName = "customProduct")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "vendor", source = "product.vendor")
    BockingProduct entityToModel(BockingProductEntity entity);

    BockingProduct modelToModel(PreOfferProduct model);

    List<BockingProduct> entitiesToModelList(List<BockingProductEntity> list);

    @Mapping(target = "product", source = "product", qualifiedByName = "customProductEntity")
    BockingProductEntity modelToEntity(BockingProduct model);

    List<BockingProductEntity> modelsToEntityList(List<BockingProduct> list);

    @Named("customerFileMappings")
    static List<FileResponse> customerFileMappings(List<FileEntity> entities) {
        return Objects.nonNull(entities) ? entities.stream()
                .map(fileEntity -> FileResponse
                        .builder()
                        .id(fileEntity.getId())
                        .creationUser(Objects.nonNull(fileEntity.getCreationUser())? User.builder()
                                .id(fileEntity.getCreationUser().getId())
                                .name(Objects.nonNull(fileEntity.getCreationUser().getName())?fileEntity.getCreationUser().getName():null)
                                .customer(Objects.nonNull(fileEntity.getCreationUser().getCustomer())? Customer.builder().id(fileEntity.getCreationUser().getCustomer().getId()).build():null)
                                .address(Objects.nonNull(fileEntity.getCreationUser().getAddress())?fileEntity.getCreationUser().getAddress():null)
                                .email(Objects.nonNull(fileEntity.getCreationUser().getEmail())? fileEntity.getCreationUser().getEmail():null)
                                .mobile(Objects.nonNull(fileEntity.getCreationUser().getMobile())?fileEntity.getCreationUser().getMobile():null)
                                .typeUser(Objects.nonNull(fileEntity.getCreationUser().getTypeUser())?fileEntity.getCreationUser().getTypeUser():null).build():null)
                        .creationTime(Objects.nonNull(fileEntity.getCreationTime()) ? fileEntity.getCreationTime() : null)
                        .extension(fileEntity.getExtension())
                        .name(fileEntity.getName())
                        .url(fileEntity.getUrl())
                        .build()).collect(Collectors.toList()) : null;
    }
    @Named("customProduct")
    default Product customProduct(final ProductEntity product) {
        return !Objects.isNull(product)
                ? Product.builder()
                .id(product.getId())
                .availabilityTo(product.getAvailabilityTo())
                .productCommission(product.getProductCommission())
                .productVat(product.getProductVat())
                .vendor(Objects.nonNull(product.getVendor())? VendorProduct.builder().vendorId(product.getVendor().getId())
                        .name(product.getVendor().getName()).build():null)
                .visibility(product.getVisibility())
                .active(product.getActive())
                .availabilityFrom(product.getAvailabilityFrom())
                .creationTime(product.getCreationTime())
                .basePrice(product.getBasePrice())
                .defaultCommission(product.getDefaultCommission())
                .name(product.getName())
                .defaultVat(product.getDefaultVat())
                .description(product.getDescription())
                .modificationTime(product.getModificationTime())
                .priceVisible(product.getPriceVisible())
                .marketing(product.getMarketing())
                .build()
                : null;
    }

    @Named("customPreOffer")
    default PreOffer customPreOffer(final PreOfferEntity entity) {
        return !Objects.isNull(entity)
                ? PreOffer.builder()
                .id(entity.getId())
                .build()
                : null;
    }

    @Named("customOffer")
    default Offer customPreOffer(final OfferEntity entity) {
        return !Objects.isNull(entity)
                ? Offer.builder()
                .id(entity.getId())
                .build()
                : null;
    }

    @Named("customProductEntity")
    default ProductEntity customProductEntity(final Product product) {
        return Objects.nonNull(product)
                ? ProductEntity.builder()
                .id(product.getId())
                .availabilityTo(product.getAvailabilityTo())
                .productCommission(product.getProductCommission())
                .productVat(product.getProductVat())
                .vendor(Objects.nonNull(product.getVendor()) ? VendorEntity.builder().id(product.getVendor().getVendorId()).build():null)
                .visibility(product.getVisibility())
                .active(product.getActive())
                .availabilityFrom(product.getAvailabilityFrom())
                .creationTime(product.getCreationTime())
                .basePrice(product.getBasePrice())
                .defaultCommission(product.getDefaultCommission())
                .name(product.getName())
                .defaultVat(product.getDefaultVat())
                .description(product.getDescription())
                .modificationTime(product.getModificationTime())
                .priceVisible(product.getPriceVisible())
                .marketing(product.getMarketing())
                .build()
                : null;
    }

}
