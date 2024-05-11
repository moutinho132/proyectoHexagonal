package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.customers.models.Order;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.OrderRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.OrderMobilResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.OrderMobileResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.OrderResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                OfferApiMapper.class,
                PaymentDetailsApiMapper.class,
                UserApiMapper.class,
        }
)
public interface OrderApiMapper {

    Order requestToModel(OrderRequest request);

    List<Order> requestToModelList(List<OrderRequest> list);

    OrderResponse modelToResponse(Order model);

    OrderMobileResponse modelToMobileResponse(Order model);

    List<OrderResponse> modelsToResponseList(List<Order> list);

    List<OrderMobilResponse> modelsToResponseMobileList(List<Order> list);

}
