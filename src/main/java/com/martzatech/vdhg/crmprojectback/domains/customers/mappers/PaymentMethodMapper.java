package com.martzatech.vdhg.crmprojectback.domains.customers.mappers;

import com.martzatech.vdhg.crmprojectback.domains.customers.entities.PaymentMethodEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.PaymentMethod;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMethodMapper {

  PaymentMethod entityToModel(PaymentMethodEntity entity);

  List<PaymentMethod> entitiesToModelList(List<PaymentMethodEntity> list);

  PaymentMethodEntity modelToEntity(PaymentMethod model);

  List<PaymentMethodEntity> modelsToEntityList(List<PaymentMethod> list);
}
