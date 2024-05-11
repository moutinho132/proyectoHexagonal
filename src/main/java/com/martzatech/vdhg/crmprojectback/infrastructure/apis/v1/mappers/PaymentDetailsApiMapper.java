package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.customers.models.PaymentDetails;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.PaymentDetailsRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.PaymentDetailsResponse;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = {
        PaymentMethodApiMapper.class
    }
)
public interface PaymentDetailsApiMapper {

  PaymentDetails requestToModel(PaymentDetailsRequest request);

  List<PaymentDetails> requestToModelList(List<PaymentDetailsRequest> list);

  PaymentDetailsResponse modelToResponse(PaymentDetails model);

  List<PaymentDetailsResponse> modelsToResponseList(List<PaymentDetails> list);
}
