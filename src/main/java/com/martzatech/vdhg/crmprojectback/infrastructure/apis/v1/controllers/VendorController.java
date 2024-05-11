package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.controllers;

import com.martzatech.vdhg.crmprojectback.application.services.SecurityManagementService;
import com.martzatech.vdhg.crmprojectback.application.services.VendorManagementService;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import com.martzatech.vdhg.crmprojectback.domains.vendors.models.Vendor;
import com.martzatech.vdhg.crmprojectback.domains.vendors.services.VendorService;
import com.martzatech.vdhg.crmprojectback.domains.vendors.specifications.VendorSpecification;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.VendorApi;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.FileApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.NoteApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.VendorApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.NoteRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.VendorRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.FileResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.NoteResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.PaginatedResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.VendorResponse;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@AllArgsConstructor
@RestController
@RequestMapping("/api/vendors")
@Validated
public class VendorController implements VendorApi {
  private VendorManagementService managementService;
  private VendorService vendorService;
  private VendorApiMapper mapper;
  private final SecurityManagementService securityManagementService;
  private NoteApiMapper noteApiMapper;
  private FileApiMapper fileApiMapper;

  @Override
  public VendorResponse save(final VendorRequest request) {
    final User currentUser = securityManagementService.findCurrentUser();
    return mapper.modelToResponse(managementService.saveVendor(mapper.requestToModel(request),currentUser));
  }

  @Override
  public FileResponse addFile(final Integer id, final MultipartFile file, final String extension) {
    return fileApiMapper.modelToResponse(managementService.addFile(id, file, extension));
  }
  @Override
  public PaginatedResponse<VendorResponse> findAll(final VendorSpecification specification,
                                                   final Integer page, final Integer size, final String direction, final String attribute) {
    final Long total = vendorService.count(specification);
    final List<Vendor> response = managementService.findAll(specification, page, size, direction, attribute);
    return PaginatedResponse.<VendorResponse>builder()
            .total(total.intValue())
            .page(page)
            .size(response.size())
            .items(mapper.modelsToResponseList(response))
            .build();
  }
  @Override
  public VendorResponse findById(final Integer id) {
    return mapper.modelToResponse(managementService.findById(id));
  }

  @Override
  public void deleteById(final Integer id) {
    managementService.deleteById(id);
  }

  @Override
  public NoteResponse saveNote(final Integer id, final NoteRequest note) {
    return noteApiMapper.modelToResponse(managementService.saveNote(id, noteApiMapper.requestToModel(note)));
  }
}
