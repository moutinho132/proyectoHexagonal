package com.martzatech.vdhg.crmprojectback.domains.customers.mappers;

import com.martzatech.vdhg.crmprojectback.domains.customers.entities.SubCategoryEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.SubCategory;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SubCategoryMapper {

  @Mapping(source = "category", target = "category", ignore = true)
  SubCategory entityToModel(SubCategoryEntity entity);

  List<SubCategory> entitiesToModelList(List<SubCategoryEntity> list);

  SubCategoryEntity modelToEntity(SubCategory model);

  List<SubCategoryEntity> modelsToEntityList(List<SubCategory> list);
}
