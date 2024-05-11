package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.controllers;

import com.martzatech.vdhg.crmprojectback.application.services.CustomerManagementService;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.ChatRoom;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.File;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.FileWallet;
import com.martzatech.vdhg.crmprojectback.domains.chat.services.ChatRoomService;
import com.martzatech.vdhg.crmprojectback.domains.chat.services.FileService;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Customer;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.CustomerService;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.CustomerSpecification;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.FileSpecification;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.WalletSpecification;
import com.martzatech.vdhg.crmprojectback.domains.wallet.services.WalletService;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.CustomerApi;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.*;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.*;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@RestController
@RequestMapping("/api/customers")
@Validated
public class CustomerController implements CustomerApi {

  private CustomerService service;

  private CustomerManagementService managementService;

  private CustomerApiMapper mapper;

  private IdentityDocumentApiMapper identityDocumentApiMapper;

  private PhoneApiMapper phoneApiMapper;

  private EmailApiMapper emailApiMapper;

  private AddressApiMapper addressApiMapper;

  private NoteApiMapper noteApiMapper;
  private LeadCustomerFileApiMapper leadCustomerFileApiMapper;
  private ChatRoomService roomService;
  private FileApiMapper fileApiMapper;
  private FileService fileService;
  private WalletService walletService;

  @Override
  public CustomerResponse save(final CustomerRequest request) {
    return mapper.modelToResponse(managementService.saveCustomer(mapper.requestToModel(request)));
  }

  @Override
  public FileResponse addFile(final Integer id, final MultipartFile file, final String extension,
                                          final String name) {
    return fileApiMapper.modelToResponse(managementService.addFile(id, file, extension, name));
  }

  @Override
  public CustomerResponse convert(final CustomerRequest request, final Integer leadId) {
    return mapper.modelToResponse(managementService.convert(mapper.requestToModel(request), leadId));
  }

  @Override
  public void changeStatus(final Integer id, final Integer statusId) {
    managementService.changeStatus(id, statusId);
  }

  @Override
  public PaginatedResponse<CustomerResponse> findAll(final CustomerSpecification specification,
      final Integer page, final Integer size, final String direction, final String attribute) {
    final Long total = service.count(specification);
    final List<Customer> response = managementService.findAll(specification, page, size, direction, attribute);
    return PaginatedResponse.<CustomerResponse>builder()
        .total(total.intValue())
        .page(page)
        .size(response.size())
        .items(mapper.modelsToResponseList(response))
        .build();
  }

  @Override
  public CustomerResponse findById(final Integer id) {
    ChatRoom room = roomService.findByIdCustomer(id);
    return mapper.modelToResponse(service.findById(id)
            .withChatRoom(Objects.nonNull(room)?room:null)
            .withFiles(fileService.getFilesCustomerNotPageable(id)));
  }

  @Override
  public void deleteCustomerFile(Integer idCustomer, Integer idFile) {
    managementService.deleteCustomerFile(idCustomer,idFile);
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
  public IdentityDocumentResponse saveIdentityDocument(final Integer customerId,
      final IdentityDocumentRequest identityDocumentRequest) {
    return identityDocumentApiMapper
        .modelToResponse(managementService
            .saveIdentityDocument(customerId, identityDocumentApiMapper.requestToModel(identityDocumentRequest)));
  }

  @Override
  public List<IdentityDocumentResponse> getIdentityDocuments(final Integer customerId) {
    return identityDocumentApiMapper.modelsToResponseList(managementService.getIdentityDocuments(customerId));
  }

  @Override
  public PaginatedResponse<FileResponse> getAllFileCustomer(Integer customerId, final FileSpecification specification,
                                                            final Integer page, final Integer size,
                                                            final String direction, final String attribute) {
    final Sort.Direction directionEnum =
            Arrays.stream(Sort.Direction.values()).anyMatch(v -> v.name().equalsIgnoreCase(direction))
                    ? Sort.Direction.fromString(direction)
                    : Sort.Direction.DESC;
    final Sort sort = Sort.by(directionEnum, attribute);
    final Pageable pageable = PageRequest.of(page, size, sort);

    List<File> response = fileService.getFilesCustomer(customerId,pageable);
    return PaginatedResponse.<FileResponse>builder()
            .total(response.size())
            .page(page)
            .size(response.size())
            .items(fileApiMapper.modelsToResponseList(response))
            .build();
  }

  @Override
  public void deleteIdentityDocumentById(final Integer customerId, final Integer identityDocumentId) {
    managementService.deleteIdentityDocumentById(customerId, identityDocumentId);
  }

  @Override
  public PhoneResponse savePhone(final Integer customerId, final PhoneRequest phoneRequest) {
    return phoneApiMapper
        .modelToResponse(managementService.savePhone(customerId, phoneApiMapper.requestToModel(phoneRequest)));
  }

  @Override
  public List<PhoneResponse> getPhones(final Integer customerId) {
    return phoneApiMapper.modelsToResponseList(managementService.getPhones(customerId));
  }

  @Override
  public void validatePhone(final Integer customerId, final Integer phoneId, final Boolean valid) {
    managementService.validatePhone(customerId, phoneId, valid);
  }

  @Override
  public void deletePhoneById(final Integer customerId, final Integer phoneId) {
    managementService.deletePhoneById(customerId, phoneId);
  }

  @Override
  public EmailResponse saveEmail(final Integer CustomerId, final EmailRequest emailRequest) {
    return emailApiMapper
        .modelToResponse(managementService.saveEmail(CustomerId, emailApiMapper.requestToModel(emailRequest)));
  }

  @Override
  public List<EmailResponse> getEmails(final Integer customerId) {
    return emailApiMapper.modelsToResponseList(managementService.getEmails(customerId));
  }

  @Override
  public void validateEmail(final Integer customerId, final Integer emailId, final Boolean valid) {
    managementService.validateEmail(customerId, emailId, valid);
  }

  @Override
  public void deleteEmailById(final Integer customerId, final Integer emailId) {
    managementService.deleteEmailById(customerId, emailId);
  }

  @Override
  public AddressResponse saveAddress(final Integer customerId, final AddressRequest addressRequest) {
    return addressApiMapper
        .modelToResponse(managementService.saveAddress(customerId, addressApiMapper.requestToModel(addressRequest)));
  }

  @Override
  public List<AddressResponse> getAddresses(final Integer customerId) {
    return addressApiMapper.modelsToResponseList(managementService.getAddress(customerId));
  }

  @Override
  public void deleteAddressById(final Integer customerId, final Integer addressId) {
    managementService.deleteAddressById(customerId, addressId);
  }

  @Override
  public NoteResponse saveNote(final Integer id, final NoteRequest note) {
    return noteApiMapper.modelToResponse(managementService.saveNote(id, noteApiMapper.requestToModel(note)));
  }

  @Override
  public FileWalletResponse addWallet(Integer id, MultipartFile file, String extension, String name,String text) {
    FileRequest fileRequest = new FileRequest(file,name,extension,text);
    return fileApiMapper.modelToWalletResponse(managementService.addWallet(id,fileRequest));
  }

  @Override
  public PaginatedResponse<FileWalletResponse> findByWalletCustomer(final Integer id, final WalletSpecification specification, final Integer page, final Integer size,
                                                              final String direction, final String attribute) {
    final Long total = walletService.count(specification);
    final Sort.Direction directionEnum =
            Arrays.stream(Sort.Direction.values()).anyMatch(v -> v.name().equalsIgnoreCase(direction))
                    ? Sort.Direction.fromString(direction)
                    : Sort.Direction.DESC;
    final Sort sort = Sort.by(directionEnum, attribute);
    final Pageable pageable = PageRequest.of(page, size, sort);
    List<FileWallet> response = managementService.getWallet(id,specification,pageable);
    return PaginatedResponse.<FileWalletResponse>builder()
            .total(total.intValue())
            .page(page)
            .size(response.size())
            .items(fileApiMapper.modelsToResponseWalletList(response))
            .build();
  }
}
