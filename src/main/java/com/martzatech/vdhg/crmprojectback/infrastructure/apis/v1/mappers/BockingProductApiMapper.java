package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.customers.models.BockingProduct;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.ProductPriceRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.ProductOfferPriceBokingsResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.ProductPreOfferPriceBokingsResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.ProductPriceResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                ProductApiMapper.class
        }
)
public interface BockingProductApiMapper {

    BockingProduct requestToModel(ProductPriceRequest request);

    List<BockingProduct> requestToModelList(List<ProductPriceRequest> list);

    ProductPriceResponse modelToResponse(BockingProduct model);

   // @Mapping(source = "product.id", target = "productId")
   // @Mapping(source = "files", target = "files", qualifiedByName = "fileEntityListToFileList")
    ProductOfferPriceBokingsResponse modelToBockingResponse(BockingProduct model);

    ProductPreOfferPriceBokingsResponse modelToBockingPreOfferResponse(BockingProduct model);

    List<ProductPriceResponse> modelsToResponseList(List<BockingProduct> list);
}
