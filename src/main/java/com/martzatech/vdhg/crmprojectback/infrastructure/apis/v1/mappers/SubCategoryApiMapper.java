package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.customers.models.SubCategory;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.SubCategoryRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.SubCategoryResponse;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubCategoryApiMapper {

  SubCategory requestToModel(SubCategoryRequest request);

  List<SubCategory> requestToModelList(List<SubCategoryRequest> list);

  SubCategoryResponse modelToResponse(SubCategory model);

  List<SubCategoryResponse> modelsToResponseList(List<SubCategory> list);
}
