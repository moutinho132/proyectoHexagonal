package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.controllers;

import com.martzatech.vdhg.crmprojectback.application.services.OfferManagementService;
import com.martzatech.vdhg.crmprojectback.domains.chat.services.FileService;
import com.martzatech.vdhg.crmprojectback.domains.customers.enums.OfferStatusEnum;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Offer;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.OfferService;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.OfferSpecification;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.OfferApi;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.FileApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.OfferApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.PreOfferProductApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.OfferRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.PreOfferProductRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.FileResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.OfferResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.PaginatedResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.StatusResponse;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/api/offers")
@Validated
public class OfferController implements OfferApi {

  private OfferService service;
  private OfferManagementService managementService;
  private OfferApiMapper mapper;
  private FileService fileService;
  private PreOfferProductApiMapper preOfferProductApiMapper;
  private FileApiMapper fileApiMapper;
  @Override
  public OfferResponse save(final OfferRequest request) {
      return mapper.modelToResponse(managementService.save(mapper.requestToModel(request)));
  }

  @Override
  public FileResponse addFile(final Integer id, Integer productId, final MultipartFile file, final String extension) {
    return  fileApiMapper.modelToResponse(managementService.addFile(id,productId, file, extension));
  }

  @Override
  public OfferResponse saveProduct(PreOfferProductRequest request, Integer id) {
    return mapper.modelToResponse(managementService
            .saveProduct(id,preOfferProductApiMapper.requestToModel(request)));
  }

  @Override
  public boolean sendEmailAndChat( Integer id) {
     return managementService.sendEmailAndChat(id);
  }

  @Override
  public List<StatusResponse> getStatus() {
    return Arrays.stream(OfferStatusEnum.values())
        .map(e -> StatusResponse.builder().id(e.getId()).name(e.name()).build())
        .collect(Collectors.toList());
  }

  @Override
  public PaginatedResponse<OfferResponse> findAll(final OfferSpecification specification, final Integer page,
      final Integer size, final String direction, final String attribute) {
    final Long total = service.count(specification);
    final List<Offer> response = managementService.findAll(specification, page, size, direction, attribute);
    return PaginatedResponse.<OfferResponse>builder()
        .total(total.intValue())
        .page(page)
        .size(response.size())
        .items(mapper.modelsToResponseList(response))
        .build();
  }

  @Override
  public OfferResponse findById(final Integer id) {
    return mapper.modelToResponse(service.findById(id));
  }

  @Override
  public List<OfferResponse> findByNumber(final Integer number) {
    return mapper.modelsToResponseList(service.findByNumber(number)
            .stream()
            .collect(Collectors.toList()));
  }

  @Override
  public void deleteById(final Integer id) {
    managementService.deleteById(id);
  }

 /* @Override
  public void deleteStatus(final Integer id) {
    managementService.deleteStatus(id);
  }*/



  @Override
  public void deleteByNumber(final Integer number) {
    managementService.deleteByNumber(number);
  }

  @Override
  public Map<String, String> getPdfById(final Integer number) throws IOException {
    return managementService.getPdfById(number);
  }

  @Override
  public void closeById(final Integer number) {
    managementService.closeById(number);
  }

  @Override
  public void deleteByFileIdAndProductId(Integer id, Integer productId, Integer fileId) {
    managementService.deleteProductFileOffer(productId,id,fileId);
  }
  @Override
  public void deleteByIdAndProductId(Integer id, Integer productId) {
    managementService.deleteProductOffer(productId,id);
  }

}
