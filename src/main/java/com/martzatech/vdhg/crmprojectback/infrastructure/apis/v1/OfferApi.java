package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1;

import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.OfferSpecification;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.OfferRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.PreOfferProductRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.FileResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.OfferResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.PaginatedResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.StatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface OfferApi {

  @Operation(summary = "Save a Offer")
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  OfferResponse save(@RequestBody @Valid OfferRequest request);
  @Operation(summary = "Add a product file associated with an offer")
  @PostMapping("{id}/products/{productId}/files")
  @ResponseStatus(HttpStatus.OK)
  FileResponse addFile(
          @PathVariable("id") Integer id,
          @PathVariable("productId") Integer productId,
          @RequestParam("file") MultipartFile file,
          @RequestParam("extension") String extension
  );
  @Operation(summary = "Save Product a Offer")
  @PostMapping(value = "/{id}/products",consumes = MediaType.APPLICATION_JSON_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  OfferResponse saveProduct(@RequestBody @Valid PreOfferProductRequest request,
                               @PathVariable("id") Integer id);

  @Operation(summary = "Offer Send Email")
  @PostMapping(value = "/{id}/send",consumes = MediaType.APPLICATION_JSON_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  boolean sendEmailAndChat(@PathVariable("id") Integer id);

  @Operation(summary = "Get all offer status")
  @GetMapping(path = "/status", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  List<StatusResponse> getStatus();

  @Operation(summary = "Get all offers")
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  PaginatedResponse<OfferResponse> findAll(OfferSpecification specification,
      @RequestParam(name = "page", defaultValue = "0") Integer page,
      @RequestParam(name = "size", defaultValue = "10") Integer size,
      @RequestParam(name = "sort-direction", defaultValue = "DESC") String direction,
      @RequestParam(name = "sort-attribute", defaultValue = "number") String attribute);

  @Operation(summary = "Get a Offer by its id")
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  OfferResponse findById(@PathVariable("id") Integer id);

  @Operation(summary = "Get a Offer by its number")
  @GetMapping(value = "/number/{number}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  List<OfferResponse> findByNumber(@PathVariable("number") Integer number);

  /*@Operation(summary = "Delete a Offer by its id soft delete")
  @DeleteMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  void deleteStatus(@PathVariable("id") Integer id);*/

  @Operation(summary = "Delete a Offer by its id hard delete")
  @DeleteMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  void deleteById(@PathVariable("id") Integer id);

  @Operation(summary = "Delete a Offer by its number")
  @DeleteMapping(value = "/number/{number}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.ACCEPTED)
  void deleteByNumber(@PathVariable("number") Integer number);

  @Operation(summary = "Delete a product file from an Offer")
  @DeleteMapping(value = "/{id}/products/{productId}/files/{fileId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.ACCEPTED)
  void deleteByFileIdAndProductId(@PathVariable("id") Integer id, @PathVariable("productId") Integer productId,
                                  @PathVariable("fileId") Integer fileId);

  @Operation(summary = "Get a Offer pdf by its id and update to confirmed")
  @PostMapping(value = "/{id}/pdf", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  Map<String, String> getPdfById(@PathVariable("id") Integer id) throws IOException;

  @Operation(summary = "Close a offer")
  @PostMapping(value = "/{id}/close")
  @ResponseBody
  @ResponseStatus(HttpStatus.ACCEPTED)
  void closeById(@PathVariable("id") Integer id);

  @Operation(summary = "Delete a product from an Offer")
  @DeleteMapping(value = "/{id}/products/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.ACCEPTED)
  void deleteByIdAndProductId(@PathVariable("id") Integer id,@PathVariable("productId") Integer productId);


}
