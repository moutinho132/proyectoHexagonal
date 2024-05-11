package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.controllers;

import com.martzatech.vdhg.crmprojectback.application.services.CategoryManagementService;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.CategoryApi;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.CategoryApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.CategoryRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.CategoryResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/categories")
@Validated
public class CategoryController implements CategoryApi {

  private CategoryManagementService service;

  private CategoryApiMapper mapper;

  @Override
  public CategoryResponse save(final CategoryRequest request) {
    return mapper.modelToResponse(service.save(mapper.requestToModel(request)));
  }

  @Override
  public List<CategoryResponse> findAll() {
    return mapper.modelsToResponseList(service.findAll());
  }

  @Override
  public CategoryResponse findById(final Integer id) {
    return mapper.modelToResponse(service.findById(id));
  }

  @Override
  public void deleteById(final Integer id) {
    service.deleteById(id);
  }
}
