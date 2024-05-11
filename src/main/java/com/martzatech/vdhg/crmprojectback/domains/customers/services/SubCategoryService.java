package com.martzatech.vdhg.crmprojectback.domains.customers.services;

import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.SubCategoryNotFoundException;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.SubCategoryEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.mappers.SubCategoryMapper;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.SubCategory;
import com.martzatech.vdhg.crmprojectback.domains.customers.repositories.SubCategoryRepository;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@AllArgsConstructor
@Slf4j
@Service
public class SubCategoryService {

  private SubCategoryRepository repository;
  private SubCategoryMapper mapper;

  public SubCategory save(final SubCategory model) {
    if (Objects.requireNonNullElse(model.getId(), 0) != 0) {
      existsById(model.getId());
    }
    final SubCategoryEntity saved = repository.save(mapper.modelToEntity(model));
    return mapper.entityToModel(saved);
  }

  public List<SubCategory> findAll() {
    return mapper.entitiesToModelList(repository.findAll());
  }

  public SubCategory findById(final Integer id) {
    return mapper.entityToModel(repository.findById(id).orElseThrow(() -> new SubCategoryNotFoundException(id)));
  }

  public void deleteById(final Integer id) {
    existsById(id);

    repository.deleteById(id);
  }

  public void existsById(final Integer id) {
    if (!repository.existsById(id)) {
      throw new SubCategoryNotFoundException(id);
    }
  }

  public SubCategory findByNameAndCategoryId(final String name, final Integer categoryId) {
    return repository.findByNameAndCategoryId(name, categoryId).map(mapper::entityToModel).orElse(null);
  }

  public void keepOnlyExists(final List<Integer> subCategoryIds, final Integer categoryId) {
    final List<Integer> ids = repository.findByIdsByCategoryId(categoryId);
    if (!CollectionUtils.isEmpty(ids)) {
      final List<Integer> idsToDelete = CollectionUtils.isEmpty(subCategoryIds)
          ? ids
          : ids.stream()
              .filter(id -> !subCategoryIds.contains(id))
              .collect(Collectors.toList());
      if (!CollectionUtils.isEmpty(idsToDelete)) {
        idsToDelete.forEach(repository::deleteById);
        log.info("Removing not used subCategory with categoryId = {}. Total affected rows = {}", categoryId,
            idsToDelete);
      }
    }
  }
}
