package com.martzatech.vdhg.crmprojectback.domains.customers.mappers;

import com.martzatech.vdhg.crmprojectback.domains.customers.entities.PaymentDetailsEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.PaymentDetails;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = {
        PaymentMethodMapper.class
    }
)
public interface PaymentDetailsMapper {

  PaymentDetails entityToModel(PaymentDetailsEntity entity);

  List<PaymentDetails> entitiesToModelList(List<PaymentDetailsEntity> list);

  PaymentDetailsEntity modelToEntity(PaymentDetails model);

  List<PaymentDetailsEntity> modelsToEntityList(List<PaymentDetails> list);
}
