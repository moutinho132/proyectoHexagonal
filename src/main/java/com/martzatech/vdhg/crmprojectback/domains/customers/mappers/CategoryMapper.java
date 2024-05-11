package com.martzatech.vdhg.crmprojectback.domains.customers.mappers;

import com.martzatech.vdhg.crmprojectback.domains.customers.entities.CategoryEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.SubCategoryEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Category;
import java.util.ArrayList;
import java.util.List;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.util.CollectionUtils;

@Mapper(
    componentModel = "spring",
    uses = {
        SubCategoryMapper.class,
        CustomNamedMappers.class
    }
)
public interface CategoryMapper {

  @Mapping(source = "creationUser", target = "creationUser", qualifiedByName = "customUser")
  @Mapping(source = "modificationUser", target = "modificationUser", qualifiedByName = "customUser")
  Category entityToModel(CategoryEntity entity);

  List<Category> entitiesToModelList(List<CategoryEntity> list);

  @Mapping(target = "subCategories", ignore = true)
  CategoryEntity modelToEntity(Category model);

  List<CategoryEntity> modelsToEntityList(List<Category> list);

  @AfterMapping
  default void afterModelToEntity(@MappingTarget final CategoryEntity.CategoryEntityBuilder entityBuilder,
      final Category model) {
    entityBuilder.subCategories(
        CollectionUtils.isEmpty(model.getSubCategories())
            ? new ArrayList<>()
            : model.getSubCategories().stream()
                .map(subCategory -> SubCategoryEntity.builder().id(subCategory.getId()).name(subCategory.getName())
                    .build())
                .toList());
  }
}
