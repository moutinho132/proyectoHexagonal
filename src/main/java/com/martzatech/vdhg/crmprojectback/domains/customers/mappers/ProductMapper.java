package com.martzatech.vdhg.crmprojectback.domains.customers.mappers;

import com.martzatech.vdhg.crmprojectback.domains.chat.entities.FileEntity;
import com.martzatech.vdhg.crmprojectback.domains.chat.mappers.FileMapper;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.File;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.*;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Category;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Product;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.SubCategory;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.VendorProduct;
import com.martzatech.vdhg.crmprojectback.domains.security.entities.UserEntity;
import com.martzatech.vdhg.crmprojectback.domains.security.mappers.CommonNamed;
import com.martzatech.vdhg.crmprojectback.domains.security.mappers.SubsidiaryMapper;
import com.martzatech.vdhg.crmprojectback.domains.security.models.DeletedStatus;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import com.martzatech.vdhg.crmprojectback.domains.vendors.entities.VendorEntity;
import com.martzatech.vdhg.crmprojectback.domains.vendors.mapper.VendorMapper;
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
        CommonNamed.class,
        //ProductFileMapper.class,
        CategoryMapper.class,
        SubsidiaryMapper.class,
        MembershipMapper.class,
        SubCategoryMapper.class,
        VendorMapper.class,
        ProductLocationMapper.class,
        FileMapper.class,
    }
)
public interface ProductMapper {

  @Mapping(source = "creationUser", target = "creationUser",qualifiedByName = "customUserMapping")
  @Mapping(source = "modificationUser", target = "modificationUser",  qualifiedByName = "customUserMapping")
  @Mapping(source = "category", target = "category", qualifiedByName = "customCategory")
  @Mapping(source = "subCategories", target = "subCategories", qualifiedByName = "customSubCategories")
  @Mapping(source = "vendor", target = "vendor", qualifiedByName = "customVendorToEntity")
  @Mapping(source = "status", target = "status")
  @Mapping(source = "files", target = "files",qualifiedByName = "customerListFileMappings")
  Product entityToModel(ProductEntity entity);

  List<Product> entitiesToModelList(List<ProductEntity> list);

  @Mapping(source = "status", target = "status")
  @Mapping(source = "vendor", target = "vendor", qualifiedByName = "customVendor")
  ProductEntity modelToEntity(Product model);

  List<ProductEntity> modelsToEntityList(List<Product> list);

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
  @Named("customUserMapping")
  static UserEntity customUserMapping(final User model) {
    return Objects.nonNull(model)
            ? UserEntity
            .builder()
            .id(model.getId())
            .title(model.getTitle())
            .name(model.getName())
            .surname(model.getSurname())
            .email(model.getEmail())
            .address(model.getAddress())
            .mobile(model.getMobile())
            .customer(Objects.nonNull(model.getCustomer()) ? CustomerEntity.builder()
                    .id(model.getCustomer().getId())
                    .membership(MembershipEntity.builder()
                            .id(model.getCustomer().getMembership().getId())
                            .priority(model.getCustomer().getMembership().getPriority()).build())
                    .build() : null)
            .typeUser(Objects.nonNull(model.getTypeUser()) ? model.getTypeUser() : null)
            .build()
            : null;
  }

  @AfterMapping
  default void afterMappingEntity(@MappingTarget final ProductEntity.ProductEntityBuilder builder,
      final Product product) {
    if (!CollectionUtils.isEmpty(product.getMemberships()) && Objects.nonNull(product.getId())) {
      builder.memberships(
          product.getMemberships().stream()
              .map(membership ->
                  MembershipEntity.builder()
                      .id(membership.getId())
                      .build()
              )
              .collect(Collectors.toList())
      );
    } else {
      builder.memberships(new ArrayList<>());
    }

    if (!CollectionUtils.isEmpty(product.getSubCategories()) && Objects.nonNull(product.getId())) {
      builder.subCategories(
          product.getSubCategories().stream()
              .map(subCategory ->
                  SubCategoryEntity.builder()
                      .id(subCategory.getId())
                      .build()
              )
              .collect(Collectors.toList())
      );
    } else {
      builder.subCategories(new ArrayList<>());
    }
  }

  @Named("customCategory")
  static Category customCategory(final CategoryEntity entity) {
    return Objects.nonNull(entity)
        ? Category.builder().id(entity.getId())
            .name(entity.getName())
            .color(entity.getColor())
            .deleteStatus(DeletedStatus.builder().id(1).build()).build()
        : null;
  }


  @Named("customVendor")
  static VendorEntity customVendor(final VendorProduct vendorProduct) {
    return Objects.nonNull(vendorProduct)
            ? VendorEntity.builder().id(vendorProduct.getVendorId()).build()
            : null;
  }

  @Named("customVendorToEntity")
  static VendorProduct customVendorToEntity(final VendorEntity entity) {
    return Objects.nonNull(entity)
            ? VendorProduct
            .builder()
            .vendorId(entity.getId())
            .billingAddress(StringUtils.isNotBlank(entity.getBillingAddress())?entity.getBillingAddress():null)
            .commission(Objects.nonNull(entity.getCommission())?entity.getCommission():null)
            .companyReg(Objects.nonNull(entity.getCompanyReg())?entity.getCompanyReg():null)
            .creationTime(Objects.nonNull(entity.getCreationTime())?entity.getCreationTime():null)
            //.creationUser(Objects.nonNull(entity.getCreationUser())? User.builder().id(entity.getCreationUser().getId()).build():null)
            .financeEmail(Objects.nonNull(entity.getFinanceEmail())?entity.getFinanceEmail():null)
            .vat(Objects.nonNull(entity.getVat())?entity.getVat():null)
            .modificationTime(Objects.nonNull(entity.getModificationTime())?entity.getModificationTime():null)
            //.modificationUser(Objects.nonNull(entity.getModificationUser())? User.builder().id(entity.getModificationUser().getId()).build():null)
            .paymentMethod(Objects.nonNull(entity.getPaymentMethod())?entity.getPaymentMethod():null)
            .paymentDetail(Objects.nonNull(entity.getPaymentDetails())?entity.getPaymentDetails():null)
            .name(StringUtils.isNotBlank(entity.getName())?entity.getName():null)
            .build()
            : null;
  }

  @Named("customSubCategories")
  static List<SubCategory> customSubCategories(final List<SubCategoryEntity> entities) {
    return !CollectionUtils.isEmpty(entities)
        ? entities.stream()
        .map(subCategoryEntity ->
            SubCategory.builder()
                .id(subCategoryEntity.getId())
                .name(subCategoryEntity.getName())
                .build()
        )
        .collect(Collectors.toList())
        : null;
  }
}
