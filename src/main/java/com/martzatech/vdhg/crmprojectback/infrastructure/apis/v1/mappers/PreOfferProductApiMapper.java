package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.customers.models.PreOfferProduct;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.PreOfferProductRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.PreOfferProductResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
    componentModel = "spring"
)
public interface PreOfferProductApiMapper {

  PreOfferProduct requestToModel(PreOfferProductRequest request);

  List<PreOfferProduct> requestToModelList(List<PreOfferProductRequest> list);

  PreOfferProductResponse modelToResponse(PreOfferProduct model);

  List<PreOfferProductResponse> modelsToResponseList(List<PreOfferProduct> list);
}
