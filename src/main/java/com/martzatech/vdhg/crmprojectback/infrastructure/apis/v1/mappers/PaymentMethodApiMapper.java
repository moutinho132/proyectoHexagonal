package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.customers.models.PaymentMethod;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.PaymentMethodRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.PaymentMethodResponse;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMethodApiMapper {

  PaymentMethod requestToModel(PaymentMethodRequest request);

  List<PaymentMethod> requestToModelList(List<PaymentMethodRequest> list);

  PaymentMethodResponse modelToResponse(PaymentMethod model);

  List<PaymentMethodResponse> modelsToResponseList(List<PaymentMethod> list);
}
