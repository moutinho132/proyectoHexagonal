package com.martzatech.vdhg.crmprojectback.domains.customers.mappers;

import com.martzatech.vdhg.crmprojectback.domains.customers.entities.DiscountEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Discount;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring"
)
public interface DiscountMapper {

  Discount entityToModel(DiscountEntity entity);

  List<Discount> entitiesToModelList(List<DiscountEntity> list);

  DiscountEntity modelToEntity(Discount model);

  List<DiscountEntity> modelsToEntityList(List<Discount> list);
}
