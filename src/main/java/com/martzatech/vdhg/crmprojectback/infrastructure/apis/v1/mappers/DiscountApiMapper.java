package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.customers.models.Discount;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.DiscountRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.DiscountResponse;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DiscountApiMapper {

  Discount requestToModel(DiscountRequest request);

  List<Discount> requestToModelList(List<DiscountRequest> list);

  DiscountResponse modelToResponse(Discount model);

  List<DiscountResponse> modelsToResponseList(List<Discount> list);
}
