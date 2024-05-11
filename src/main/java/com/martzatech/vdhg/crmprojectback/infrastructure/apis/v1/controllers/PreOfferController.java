package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.controllers;

import com.martzatech.vdhg.crmprojectback.application.services.PreOfferManagementService;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.PreOffer;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.PreOfferService;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.PreOfferSpecification;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.PreOfferApi;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.FileApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.PreOfferApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.PreOfferProductApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.PreOfferProductRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.PreOfferRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.FileResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.PaginatedResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.PreOfferResponse;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/api/pre-offers")
@Validated
public class PreOfferController implements PreOfferApi {

  private PreOfferService service;
  private PreOfferManagementService managementService;
  private PreOfferApiMapper mapper;
  private PreOfferProductApiMapper preOfferProductApiMapper;
  private FileApiMapper fileApiMapper;

  @Override
  public PreOfferResponse save(final PreOfferRequest request) {
    return mapper.modelToResponse(managementService.save(mapper.requestToModel(request)));
  }

  @Override
  public PreOfferResponse saveProduct(PreOfferProductRequest request, Integer id) {
    return mapper.modelToResponse(managementService
            .saveProduct(id,preOfferProductApiMapper.requestToModel(request)));
  }

  @Override
  public FileResponse addFile(final Integer id,Integer productId, final MultipartFile file, final String extension) {
    return  fileApiMapper.modelToResponse(managementService.addFile(id,productId, file, extension));
  }

  @Override
  public PaginatedResponse<PreOfferResponse> findAll(final PreOfferSpecification specification, final Integer page,
      final Integer size, final String direction, final String attribute) {
    final Long total = service.count(specification);
    final List<PreOffer> response = managementService.findAll(specification, page, size, direction, attribute);
    return PaginatedResponse.<PreOfferResponse>builder()
        .total(total.intValue())
        .page(page)
        .size(response.size())
        .items(mapper.modelsToResponseList(response))
        .build();
  }

  @Override
  public PreOfferResponse findById(final Integer id) {
    return mapper.modelToResponse(service.findById(id));
  }

  @Override
  public List<PreOfferResponse> findByNumber(final Integer number) {
    return mapper.modelsToResponseList(service.findByNumber(number));
  }

  @Override
  public void deleteById(final Integer id) {
    managementService.deleteById(id);
  }

  @Override
  public void deleteByNumber(final Integer number) {
    managementService.deleteByNumber(number);
  }

  @Override
  public void deleteByFileIdAndProductId(Integer id, Integer productId, Integer fileId) {
    managementService.deleteProductFilePreOffer(productId,id,fileId);
  }

  @Override
  public void deleteByIdAndProductId(Integer id, Integer productId) {
  managementService.deleteProductPreOffer(productId,id);
  }

  @Override
  public Map<String, String> getPdfById(final Integer number) throws IOException {
    return managementService.getPdfById(number);
  }

  @Override
  public void closeById(final Integer number) {
    managementService.closeById(number);
  }
}
