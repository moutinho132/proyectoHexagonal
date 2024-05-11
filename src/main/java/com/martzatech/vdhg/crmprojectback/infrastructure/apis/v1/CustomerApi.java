package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1;

import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.CustomerSpecification;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.FileSpecification;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.WalletSpecification;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.*;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.*;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CustomerApi {

  @Operation(summary = "Save a Customer")
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  CustomerResponse save(@RequestBody @Valid CustomerRequest request);
  @Operation(summary = "Add File of a Customer")
  @PostMapping("{id}/file")
  @ResponseStatus(HttpStatus.OK)
  FileResponse addFile(
          @PathVariable("id") Integer id,
          @RequestParam("file") MultipartFile file,
          @RequestParam("extension") String extension,
          @RequestParam(value = "name",defaultValue = "") String name
  );
  @Operation(summary = "Converter a Customer")
  @PostMapping(value = "/convert/{leadId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  CustomerResponse convert(@RequestBody CustomerRequest request, @PathVariable("leadId") Integer leadId);

  @Operation(summary = "Change status of a Customer")
  @PostMapping(value = "/{id}/status/{statusId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.ACCEPTED)
  void changeStatus(@PathVariable("id") Integer id, @PathVariable("statusId") Integer leadId);

  @Operation(summary = "Get all Customers")
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  PaginatedResponse<CustomerResponse> findAll(CustomerSpecification specification,
      @RequestParam(name = "page", defaultValue = "0") Integer page,
      @RequestParam(name = "size", defaultValue = "10") Integer size,
      @RequestParam(name = "sort-direction", defaultValue = "DESC") String direction,
      @RequestParam(name = "sort-attribute", defaultValue = "id") String attribute
  );

  @Operation(summary = "Save an identity document in a exists Customer")
  @PostMapping(value = "/{id}/identity-documents", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  IdentityDocumentResponse saveIdentityDocument(@PathVariable("id") Integer customerId,
      @RequestBody IdentityDocumentRequest identityDocumentRequest);

  @Operation(summary = "Get all identity documents in a exists Customer")
  @GetMapping(value = "/{id}/identity-documents", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  List<IdentityDocumentResponse> getIdentityDocuments(@PathVariable("id") Integer customerId);


  @Operation(summary = "Get all file customer in a exists Customer")
  @GetMapping(value = "/{id}/files", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  PaginatedResponse<FileResponse> getAllFileCustomer(@PathVariable("id") Integer customerId,
                                        FileSpecification specification,
                                        @RequestParam(name = "page", defaultValue = "0") Integer page,
                                        @RequestParam(name = "size", defaultValue = "10") Integer size,
                                        @RequestParam(name = "sort-direction", defaultValue = "DESC") String direction,
                                        @RequestParam(name = "sort-attribute", defaultValue = "id") String attribute);

  @Operation(summary = "Delete an identity document by its id")
  @DeleteMapping(value = "/{id}/identity-documents/{identityDocumentId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.ACCEPTED)
  void deleteIdentityDocumentById(@PathVariable("id") Integer customerId,
      @PathVariable("identityDocumentId") Integer identityDocumentId);

  @Operation(summary = "Get a Customer by its id")
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  CustomerResponse findById(@PathVariable("id") Integer id);

  @Operation(summary = "Delete a Customer File by its id hard delete")
  @DeleteMapping(value = "/{idCustomer}/files/{idFile}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.ACCEPTED)
  void deleteCustomerFile(@PathVariable("idCustomer") Integer idCustomer,@PathVariable("idFile") Integer idFile);

  @Operation(summary = "Delete a Customer  by its id hard delete")
  @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.ACCEPTED)
  void deleteById(@PathVariable("id") Integer id);

  @Operation(summary = "Delete a Customer by its id soft delete")
  @DeleteMapping(value = "/deleteById/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.ACCEPTED)
  void deleteByStatus(@PathVariable("id") Integer id);

  @Operation(summary = "Save an phone in a exists Customer")
  @PostMapping(value = "/{id}/phones", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  PhoneResponse savePhone(@PathVariable("id") Integer customerId, @RequestBody PhoneRequest phoneRequest);

  @Operation(summary = "Get all phone in a exists Customer")
  @GetMapping(value = "/{id}/phones", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  List<PhoneResponse> getPhones(@PathVariable("id") Integer customerId);

  @Operation(summary = "Validate a phone by its id")
  @PostMapping(value = "/{id}/phones/{phoneId}/validate/{valid}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.ACCEPTED)
  void validatePhone(@PathVariable("id") Integer customerId, @PathVariable("phoneId") Integer phoneId,
      @PathVariable("valid") Boolean valid);

  @Operation(summary = "Delete a phone by its id")
  @DeleteMapping(value = "/{id}/phones/{phoneId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.ACCEPTED)
  void deletePhoneById(@PathVariable("id") Integer customerId, @PathVariable("phoneId") Integer phoneId);

  @Operation(summary = "Save an email in a exists Customer")
  @PostMapping(value = "/{id}/emails", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  EmailResponse saveEmail(@PathVariable("id") Integer customerId, @RequestBody EmailRequest emailRequest);

  @Operation(summary = "Get all emails in a exists Customer")
  @GetMapping(value = "/{id}/emails", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  List<EmailResponse> getEmails(@PathVariable("id") Integer customerId);

  @Operation(summary = "Validate a email by its id")
  @PostMapping(value = "/{id}/emails/{emailId}/validate/{valid}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.ACCEPTED)
  void validateEmail(@PathVariable("id") Integer customerId, @PathVariable("emailId") Integer emailId,
      @PathVariable("valid") Boolean valid);

  @Operation(summary = "Delete a email by its id")
  @DeleteMapping(value = "/{id}/emails/{emailId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.ACCEPTED)
  void deleteEmailById(@PathVariable("id") Integer customerId, @PathVariable("emailId") Integer emailId);

  @Operation(summary = "Save an address in a exists Customer")
  @PostMapping(value = "/{id}/address", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  AddressResponse saveAddress(@PathVariable("id") Integer customerId, @RequestBody AddressRequest addressRequest);

  @Operation(summary = "Get all addresses in a exists Customer")
  @GetMapping(value = "/{id}/address", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  List<AddressResponse> getAddresses(@PathVariable("id") Integer customerId);

  @Operation(summary = "Delete an address by its id")
  @DeleteMapping(value = "/{id}/address/{addressId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.ACCEPTED)
  void deleteAddressById(@PathVariable("id") Integer customerId, @PathVariable("addressId") Integer addressId);

  @Operation(summary = "Create note to customer")
  @PostMapping(value = "/{id}/notes", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  NoteResponse saveNote(@PathVariable("id") Integer id, @RequestBody @Valid NoteRequest note);

  @Operation(summary = "Add wallet File of a Customer")
  @PostMapping("{id}/wallet")
  @ResponseStatus(HttpStatus.OK)
  FileWalletResponse addWallet(
          @PathVariable("id") Integer id,
          @RequestParam("file") MultipartFile file,
          @RequestParam("extension") String extension,
          @RequestParam(value = "name",defaultValue = "") String name,
          @RequestParam(value = "text",defaultValue = "") String text
  );
  @Operation(summary = "Get a Customer by its id")
  @GetMapping(value = "/{id}/wallet", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  PaginatedResponse<FileWalletResponse> findByWalletCustomer(@PathVariable("id") Integer id, final WalletSpecification specification,
                                          @RequestParam(name = "page", defaultValue = "0") Integer page,
                                          @RequestParam(name = "size", defaultValue = "10") Integer size,
                                          @RequestParam(name = "sort-direction", defaultValue = "DESC") String direction,
                                          @RequestParam(name = "sort-attribute", defaultValue = "id") String attribute);

}
