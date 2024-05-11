package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.customers.models.Category;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.CategoryRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.CategoryResponse;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = {
        SubCategoryApiMapper.class
    }
)
public interface CategoryApiMapper {

  Category requestToModel(CategoryRequest request);

  List<Category> requestToModelList(List<CategoryRequest> list);

  CategoryResponse modelToResponse(Category model);

  List<CategoryResponse> modelsToResponseList(List<Category> list);
}
