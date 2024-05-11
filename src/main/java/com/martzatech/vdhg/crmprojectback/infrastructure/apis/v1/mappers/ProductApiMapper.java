package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.customers.mappers.ProductLocationMapper;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Product;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.ProductRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.ProductResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                ProductFileApiMapper.class,
                CategoryApiMapper.class,
                SubCategoryApiMapper.class,
                SubsidiaryApiMapper.class,
                MembershipApiMapper.class,
                UserApiMapper.class,
                ProductLocationMapper.class
        }
)
public interface ProductApiMapper {

    Product requestToModel(ProductRequest request);

    List<Product> requestToModelList(List<ProductRequest> list);

    ProductResponse modelToResponse(Product model);

    List<ProductResponse> modelsToResponseList(List<Product> list);
}
