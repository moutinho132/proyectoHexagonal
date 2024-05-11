package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.controllers;

import com.martzatech.vdhg.crmprojectback.application.services.ProductManagementService;
import com.martzatech.vdhg.crmprojectback.domains.chat.services.FileService;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Product;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.ProductSpecification;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.ProductApi;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.FileApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.NoteApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.ProductApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.ProductFileApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.NoteRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.ProductRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.NoteResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.PaginatedResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.ProductFileResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.ProductResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductController implements ProductApi {

  private final ProductManagementService managementService;
  private final ProductApiMapper mapper;
  private final ProductFileApiMapper productFileApiMapper;
  private final NoteApiMapper noteApiMapper;
  private final FileApiMapper fileApiMapper;
  private FileService fileService;


  @Override
  public ProductResponse save(@Valid final ProductRequest request) {
    return mapper.modelToResponse(managementService.save(mapper.requestToModel(request)));
  }

  @Override
  public ProductFileResponse addPicture(final Integer id, final MultipartFile file, final String extension) {
    return productFileApiMapper.modelToResponse(managementService.addPicture(id, file, extension));
  }

  @Override
  public PaginatedResponse<ProductResponse> findAll(final ProductSpecification specification,
                                                    final Integer page, final Integer size, final String direction,
                                                    final String attribute) {
    final Long total = managementService.count(specification);
    final List<Product> response = managementService.findAll(specification, page, size, direction, attribute);
    return PaginatedResponse.<ProductResponse>builder()
        .total(total.intValue())
        .page(page)
        .size(response.size())
        .items(mapper.modelsToResponseList(response))
        .build();
  }

  @Override
  public ProductResponse findById(final Integer id) {
    return mapper.modelToResponse(managementService.findById(id)
            .withFiles(fileService.getFileProduct(id)));
  }

  @Override
  public void deleteById(final Integer id) {
    managementService.deleteStatus(id);
  }

  @Override
  public void deleteByStatusAndId(final Integer id) {
    managementService.deleteByStatusAndId(id);
  }

  @Override
  public void deletePicture(final Integer id, final Integer fileId) {
    managementService.deletePicture(id, fileId);
  }

  @Override
  public NoteResponse saveNote(final Integer id, final NoteRequest note) {
    return noteApiMapper.modelToResponse(managementService.saveNote(id, noteApiMapper.requestToModel(note)));
  }
}
