package com.martzatech.vdhg.crmprojectback.domains.customers.services;

import com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants;
import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.CategoryNotFoundException;
import com.martzatech.vdhg.crmprojectback.domains.customers.mappers.CategoryMapper;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Category;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.SubCategory;
import com.martzatech.vdhg.crmprojectback.domains.customers.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Slf4j
@Service
public class CategoryService {

    private CategoryRepository repository;
    private CategoryMapper mapper;

    public Category save(final Category model) {
        if (Objects.requireNonNullElse(model.getId(), 0) != 0) {
            existsById(model.getId());
        }

        return sortSubByName(mapper.entityToModel(repository.save(mapper.modelToEntity(model))));
    }

    private Category sortSubByName(final Category category) {
        if (Objects.nonNull(category) && !CollectionUtils.isEmpty(category.getSubCategories())) {
            return category.withSubCategories(
                    category.getSubCategories().stream()
                            .sorted(Comparator.comparing(SubCategory::getName))
                            .toList()
            );
        }
        return category;
    }

    public List<Category> findAll() {
        return mapper.entitiesToModelList(
                        repository.findAll(Sort.by(CommonConstants.NAME_FIELD))
                )
                .stream()
                .map(this::sortSubByName)
                .toList();
    }

    public Category findById(final Integer id) {
        return sortSubByName(
                mapper.entityToModel(repository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id))));
    }

    public void deleteById(final Integer id) {
        existsById(id);

        repository.deleteById(id);
    }

    public void existsById(final Integer id) {
        if (!repository.existsById(id)) {
            throw new CategoryNotFoundException(id);
        }
    }

    public Category findByName(final String name) {
        return repository.findByNameIgnoreCase(name).map(mapper::entityToModel).orElse(null);
    }
}
