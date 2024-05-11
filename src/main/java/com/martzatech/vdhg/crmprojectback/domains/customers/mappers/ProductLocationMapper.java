package com.martzatech.vdhg.crmprojectback.domains.customers.mappers;

import com.martzatech.vdhg.crmprojectback.domains.customers.entities.ProductLocationEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.ProductLocation;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
    componentModel = "spring",
    uses = {
        ProductMapper.class
    }
)
public interface ProductLocationMapper {
  ProductLocation entityToModel(ProductLocationEntity entity);

  List<ProductLocation> entitiesToModelList(List<ProductLocationEntity> list);

  ProductLocationEntity modelToEntity(ProductLocation model);

  List<ProductLocationEntity> modelsToEntityList(List<ProductLocation> list);
}
