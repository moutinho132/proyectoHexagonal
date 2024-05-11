package com.martzatech.vdhg.crmprojectback.application.services;

import com.martzatech.vdhg.crmprojectback.application.enums.DeletedStatusEnum;
import com.martzatech.vdhg.crmprojectback.application.exceptions.BusinessRuleException;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Category;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Product;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.SubCategory;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.CategoryService;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.ProductService;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.SubCategoryService;
import com.martzatech.vdhg.crmprojectback.domains.security.models.DeletedStatus;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
@Service
public class CategoryManagementService {

    private ProductService productService;
    private CategoryService categoryService;
    private SubCategoryService subCategoryService;
    private SecurityManagementService securityManagementService;

    public Category save(final Category model) {
        validations(model);
        return build(model);
    }

    private void validations(final Category model) {
        validateCategory(model);
        validateSubCategories(model);
        //Validar que el producto se encuentre activo
    }

    private void validateSubCategories(final Category model) {
        if (!CollectionUtils.isEmpty(model.getSubCategories())) {
            model.getSubCategories().stream()
                    .filter(subCategory -> Objects.nonNull(subCategory.getId()))
                    .forEach(subCategory -> subCategoryService.existsById(subCategory.getId()));
        }

        if (!CollectionUtils.isEmpty(model.getSubCategories())) {
            final Map<String, Long> groupedByName = model.getSubCategories().stream()
                    .map(SubCategory::getName)
                    .collect(Collectors.groupingBy(name -> name, Collectors.counting()));
            groupedByName
                    .entrySet()
                    .stream()
                    .filter(entry -> entry.getValue() > 1)
                    .forEach(
                            entry -> {
                                throw new BusinessRuleException(
                                        String.format("The subcategory name: %s is duplicated", entry.getKey()));
                            });
        }

        if (Objects.nonNull(model.getId())) {
            final Category category = categoryService.findById(model.getId());
            if (!CollectionUtils.isEmpty(category.getSubCategories())) {
                final List<Integer> currentIds = CollectionUtils.isEmpty(model.getSubCategories())
                        ? List.of()
                        : model.getSubCategories().stream()
                        .map(SubCategory::getId).filter(Objects::nonNull).toList();
                category.getSubCategories()
                        .stream()
                        .filter(subCategory -> !currentIds.contains(subCategory.getId()))
                        .forEach(subCategory -> {
                            final List<Product> products = productService.findProductsBySubCategory(subCategory.getId());
                            if (!CollectionUtils.isEmpty(products) && (products.stream()
                                    .filter(e-> e.getStatus().getId().equals(DeletedStatusEnum.ACTIVE.getId()))
                                    .findAny().isPresent())) {
                                throw new BusinessRuleException(
                                        String.format("Cannot delete subcategory %d. It is related with %d products. "
                                                        + "When update and the id is not passed, the old subCategories are deleted and a new one is created",
                                                subCategory.getId(), products.size())
                                );
                            }
                        });
            }
        }
        if (Objects.nonNull(model.getId()) && !CollectionUtils.isEmpty(model.getSubCategories())) {
            subCategoryService.keepOnlyExists(
                    model.getSubCategories().stream()
                            .filter(distinctByKey(SubCategory::getName))
                            .map(SubCategory::getId).filter(Objects::nonNull)
                            .toList(),
                    model.getId()
            );
            model.getSubCategories()
                    .forEach(
                            subCategory -> {
                                final SubCategory subByName = subCategoryService
                                        .findByNameAndCategoryId(subCategory.getName(), model.getId());
                                if (Objects.nonNull(subByName)
                                        && (Objects.isNull(subCategory.getId()) || subCategory.getId().intValue() != subByName.getId())) {
                                    throw new BusinessRuleException(
                                            String.format("SubCategory with name '%s', exists by category '%s'!",
                                                    subByName.getName(),
                                                    model.getName())
                                    );
                                }
                            }
                    );
        }
    }

    private void validateCategory(final Category model) {
        if (Objects.nonNull(model.getId())) {
            categoryService.existsById(model.getId());
        }
        final Category byName = categoryService.findByName(model.getName());
        if (Objects.nonNull(byName)
                && (Objects.isNull(model.getId()) || model.getId().intValue() != byName.getId())) {
            throw new BusinessRuleException(String.format("Category with name '%s', exists!", model.getName()));
        }
    }

    private Category build(final Category model) {
        final Category fromDDBB = fromDDBB(model).withDeleteStatus(DeletedStatus.builder().id(1).build());
        final User currentUser = securityManagementService.findCurrentUser();
        final Category saved = categoryService.save(
                fromDDBB
                        .withName(model.getName())
                        .withColor(model.getColor())
                        .withCreationTime(
                                Objects.nonNull(fromDDBB.getCreationTime())
                                        ? fromDDBB.getCreationTime()
                                        : LocalDateTime.now()
                        )
                        .withCreationUser(
                                Objects.nonNull(fromDDBB.getCreationUser())
                                        ? fromDDBB.getCreationUser()
                                        : currentUser
                        )
                        .withModificationTime(
                                Objects.nonNull(model.getId())
                                        ? LocalDateTime.now()
                                        : null
                        )
                        .withModificationUser(
                                Objects.nonNull(model.getId())
                                        ? currentUser
                                        : null
                        )
        );
        return saved.withSubCategories(addSubCategories(model, saved.getId()));
    }

    private Category fromDDBB(final Category model) {
        if (Objects.nonNull(model.getId())) {
            return categoryService.findById(model.getId());
        }
        return model;
    }

    private List<SubCategory> addSubCategories(final Category model, final Integer categoryId) {
        if (!CollectionUtils.isEmpty(model.getSubCategories())) {
            return model.getSubCategories().stream()
                    .filter(distinctByKey(SubCategory::getName))
                    .map(subCategory -> subCategoryService
                            .save(subCategory.withCategory(Category.builder().id(categoryId).build())))
                    .toList();
        }
        return Collections.emptyList();
    }

    public static <T> Predicate<T> distinctByKey(final Function<? super T, Object> keyExtractor) {
        final Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    public void deleteById(final Integer id) {
        validateSubCategoriesProduct(id);
        categoryService.deleteById(id);
    }

    private void validateSubCategoriesProduct(Integer id) {
        final Category model = categoryService.findById(id);
        if (Objects.nonNull(model.getId())) {
            if (!CollectionUtils.isEmpty(model.getSubCategories())) {
                final List<Integer> currentIds = CollectionUtils.isEmpty(model.getSubCategories())
                        ? List.of()
                        : model.getSubCategories().stream()
                        .map(SubCategory::getId).filter(Objects::nonNull).toList();
                model.getSubCategories()
                        .stream()
                        .forEach(subCategory -> {
                            final List<Product> products = productService.findProductsBySubCategory(subCategory.getId());
                            if (!CollectionUtils.isEmpty(products) && (products.stream()
                                    .filter(e-> e.getStatus().getId().equals(DeletedStatusEnum.ACTIVE.getId()))
                                    .findAny().isPresent())) {
                                throw new BusinessRuleException(
                                        String.format("Cannot delete subcategory %d. It is related with %d products. "
                                                        + "When update and the id is not passed, the old subCategories are deleted and a new one is created",
                                                subCategory.getId(), products.size())
                                );

                            }
                        });
            }
        }
    }

    public List<Category> findAll() {
        return categoryService.findAll();
    }

    public Category findById(final Integer id) {
        return categoryService.findById(id);
    }
}
