package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1;

import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.LeadSpecification;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.AddressRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.EmailRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.IdentityDocumentRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.LeadRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.NoteRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.PhoneRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.*;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

public interface LeadApi {

  @Operation(summary = "Save a Lead")
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  LeadResponse save(@RequestBody @Valid LeadRequest request);

  @Operation(summary = "Add File of a Lead")
  @PostMapping("{id}/file")
  @ResponseStatus(HttpStatus.OK)
  LeadCustomerFileResponse addFile(
          @PathVariable("id") Integer id,
          @RequestParam("file") MultipartFile file,
          @RequestParam("extension") String extension,
          @RequestParam("name") String name
  );

  @Operation(summary = "Get all Leads")
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  PaginatedResponse<LeadResponse> findAll(LeadSpecification specification,
      @RequestParam(name = "page", defaultValue = "0") Integer page,
      @RequestParam(name = "size", defaultValue = "10") Integer size,
      @RequestParam(name = "sort-direction", defaultValue = "DESC") String direction,
      @RequestParam(name = "sort-attribute", defaultValue = "id") String attribute
  );

  @Operation(summary = "Save an identity document in a exists lead")
  @PostMapping(value = "/{id}/identity-documents", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  IdentityDocumentResponse saveIdentityDocument(@PathVariable("id") Integer leadId,
      @RequestBody IdentityDocumentRequest identityDocumentRequest);

  @Operation(summary = "Get all identity documents in a exists lead")
  @GetMapping(value = "/{id}/identity-documents", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  List<IdentityDocumentResponse> getIdentityDocuments(@PathVariable("id") Integer leadId);

  @Operation(summary = "Delete an identity document by its id soft delete")
  @DeleteMapping(value = "/{id}/identity-documents/{identityDocumentId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.ACCEPTED)
  void deleteIdentityDocumentById(@PathVariable("id") Integer leadId,
      @PathVariable("identityDocumentId") Integer identityDocumentId);

  @Operation(summary = "Get a Lead by its id")
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  LeadResponse findById(@PathVariable("id") Integer id);

  @Operation(summary = "Delete a Lead by its id hard delete")
  @DeleteMapping(value = "/deleteById/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.ACCEPTED)
  void deleteById(@PathVariable("id") Integer id);

  @Operation(summary = "Delete a Lead by its id soft delete")
  @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.ACCEPTED)
  void deleteByStatus(@PathVariable("id") Integer id);

  @Operation(summary = "Save an phone in a exists lead")
  @PostMapping(value = "/{id}/phones", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  PhoneResponse savePhone(@PathVariable("id") Integer leadId, @RequestBody PhoneRequest phoneRequest);

  @Operation(summary = "Get all phone in a exists lead")
  @GetMapping(value = "/{id}/phones", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  List<PhoneResponse> getPhones(@PathVariable("id") Integer leadId);

  @Operation(summary = "Validate a phone by its id")
  @PostMapping(value = "/{id}/phones/{phoneId}/validate/{valid}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.ACCEPTED)
  void validatePhone(@PathVariable("id") Integer leadId, @PathVariable("phoneId") Integer phoneId,
      @PathVariable("valid") Boolean valid);

  @Operation(summary = "Delete a phone by its id")
  @DeleteMapping(value = "/{id}/phones/{phoneId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.ACCEPTED)
  void deletePhoneById(@PathVariable("id") Integer leadId, @PathVariable("phoneId") Integer phoneId);

  @Operation(summary = "Save an email in a exists lead")
  @PostMapping(value = "/{id}/emails", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  EmailResponse saveEmail(@PathVariable("id") Integer leadId, @RequestBody EmailRequest emailRequest);

  @Operation(summary = "Get all emails in a exists lead")
  @GetMapping(value = "/{id}/emails", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  List<EmailResponse> getEmails(@PathVariable("id") Integer leadId);

  @Operation(summary = "Validate a email by its id")
  @PostMapping(value = "/{id}/emails/{emailId}/validate/{valid}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.ACCEPTED)
  void validateEmail(@PathVariable("id") Integer leadId, @PathVariable("emailId") Integer emailId,
      @PathVariable("valid") Boolean valid);

  @Operation(summary = "Delete a email by its id")
  @DeleteMapping(value = "/{id}/emails/{emailId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.ACCEPTED)
  void deleteEmailById(@PathVariable("id") Integer leadId, @PathVariable("emailId") Integer emailId);

  @Operation(summary = "Save an address in a exists lead")
  @PostMapping(value = "/{id}/address", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  AddressResponse saveAddress(@PathVariable("id") Integer leadId, @RequestBody AddressRequest addressRequest);

  @Operation(summary = "Get all addresses in a exists lead")
  @GetMapping(value = "/{id}/address", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  List<AddressResponse> getAddresses(@PathVariable("id") Integer leadId);

  @Operation(summary = "Delete an address by its id")
  @DeleteMapping(value = "/{id}/address/{addressId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.ACCEPTED)
  void deleteAddressById(@PathVariable("id") Integer leadId, @PathVariable("addressId") Integer addressId);

  @Operation(summary = "Update Status")
  @PostMapping(value = "/{id}/status/{statusId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.ACCEPTED)
  void updateStatus(@PathVariable("id") Integer leadId, @PathVariable("statusId") Integer statusId);

  @Operation(summary = "Create note to lead")
  @PostMapping(value = "/{id}/notes", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  NoteResponse saveNote(@PathVariable("id") Integer id, @RequestBody @Valid NoteRequest note);
}
