package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.customers.models.ProductFile;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.ProductFileResponse;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring"
)
public interface ProductFileApiMapper {

  ProductFileResponse modelToResponse(ProductFile model);

  List<ProductFileResponse> modelsToResponseList(List<ProductFile> list);
}
