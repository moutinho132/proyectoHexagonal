package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.controllers;

import com.martzatech.vdhg.crmprojectback.application.services.LeadManagementService;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Lead;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.LeadService;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.LeadSpecification;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.LeadApi;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.*;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.AddressRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.EmailRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.IdentityDocumentRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.LeadRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.NoteRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.PhoneRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.*;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@RestController
@RequestMapping("/api/leads")
@Valid
public class LeadController implements LeadApi {

  private LeadService service;

  private LeadManagementService managementService;

  private LeadApiMapper mapper;

  private IdentityDocumentApiMapper identityDocumentApiMapper;

  private PhoneApiMapper phoneApiMapper;

  private EmailApiMapper emailApiMapper;

  private AddressApiMapper addressApiMapper;

  private NoteApiMapper noteApiMapper;
  private LeadCustomerFileApiMapper leadCustomerFileApiMapper;

  @Override
  public LeadResponse save(final LeadRequest request) {
    return mapper.modelToResponse(managementService.save(mapper.requestToModel(request)));
  }
  @Override
  public LeadCustomerFileResponse addFile(final Integer id, final MultipartFile file, final String extension,
                                        final String name) {
    return leadCustomerFileApiMapper.modelToResponse(managementService.addFile(id, file, extension, name));
  }

  @Override
  public PaginatedResponse<LeadResponse> findAll(final LeadSpecification leadSpecification,
      final Integer page, final Integer size, final String direction, final String attribute) {
    final Long total = service.count(leadSpecification);
    final List<Lead> response = managementService.findAll(leadSpecification, page, size, direction, attribute);
    return PaginatedResponse.<LeadResponse>builder()
        .total(total.intValue())
        .page(page)
        .size(response.size())
        .items(mapper.modelsToResponseList(response))
        .build();
  }

  @Override
  public LeadResponse findById(final Integer id) {
    return mapper.modelToResponse(service.findById(id));
  }

  @Override
  public void deleteById(final Integer id) {
    managementService.deleteById(id);
  }

  @Override
  public void deleteByStatus(final Integer id) {
    managementService.deleteByStatus(id);
  }

  @Override
  public IdentityDocumentResponse saveIdentityDocument(final Integer leadId,
      final IdentityDocumentRequest identityDocumentRequest) {
    return identityDocumentApiMapper
        .modelToResponse(managementService
            .saveIdentityDocument(leadId, identityDocumentApiMapper.requestToModel(identityDocumentRequest)));
  }

  @Override
  public List<IdentityDocumentResponse> getIdentityDocuments(final Integer leadId) {
    return identityDocumentApiMapper.modelsToResponseList(managementService.getIdentityDocuments(leadId));
  }

  @Override
  public void deleteIdentityDocumentById(final Integer leadId, final Integer identityDocumentId) {
    managementService.deleteIdentityDocumentById(leadId, identityDocumentId);
  }

  @Override
  public PhoneResponse savePhone(final Integer leadId, final PhoneRequest phoneRequest) {
    return phoneApiMapper
        .modelToResponse(managementService.savePhone(leadId, phoneApiMapper.requestToModel(phoneRequest)));
  }

  @Override
  public List<PhoneResponse> getPhones(final Integer leadId) {
    return phoneApiMapper.modelsToResponseList(managementService.getPhones(leadId));
  }

  @Override
  public void validatePhone(final Integer leadId, final Integer phoneId, final Boolean valid) {
    managementService.validatePhone(leadId, phoneId, valid);
  }

  @Override
  public void deletePhoneById(final Integer leadId, final Integer phoneId) {
    managementService.deletePhoneById(leadId, phoneId);
  }

  @Override
  public EmailResponse saveEmail(final Integer leadId, final EmailRequest emailRequest) {
    return emailApiMapper
        .modelToResponse(managementService.saveEmail(leadId, emailApiMapper.requestToModel(emailRequest)));
  }

  @Override
  public List<EmailResponse> getEmails(final Integer leadId) {
    return emailApiMapper.modelsToResponseList(managementService.getEmails(leadId));
  }

  @Override
  public void validateEmail(final Integer leadId, final Integer emailId, final Boolean valid) {
    managementService.validateEmail(leadId, emailId, valid);
  }

  @Override
  public void deleteEmailById(final Integer leadId, final Integer emailId) {
    managementService.deleteEmailById(leadId, emailId);
  }

  @Override
  public AddressResponse saveAddress(final Integer leadId, final AddressRequest addressRequest) {
    return addressApiMapper
        .modelToResponse(managementService.saveAddress(leadId, addressApiMapper.requestToModel(addressRequest)));
  }

  @Override
  public List<AddressResponse> getAddresses(final Integer leadId) {
    return addressApiMapper.modelsToResponseList(managementService.getAddress(leadId));
  }

  @Override
  public void deleteAddressById(final Integer leadId, final Integer addressId) {
    managementService.deleteAddressById(leadId, addressId);
  }

  @Override
  public void updateStatus(final Integer leadId, final Integer statusId) {
    managementService.updateStatus(leadId, statusId);
  }

  @Override
  public NoteResponse saveNote(final Integer id, final NoteRequest note) {
    return noteApiMapper.modelToResponse(managementService.saveNote(id, noteApiMapper.requestToModel(note)));
  }
}
