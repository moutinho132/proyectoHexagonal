package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1;

import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.PreOfferSpecification;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.PreOfferProductRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.PreOfferRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.FileResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.PaginatedResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.PreOfferResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface PreOfferApi {

  @Operation(summary = "Save a PreOffer")
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  PreOfferResponse save(@RequestBody @Valid PreOfferRequest request);

  @Operation(summary = "Save Product a PreOffer")
  @PostMapping(value = "/{id}/products",consumes = MediaType.APPLICATION_JSON_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  PreOfferResponse saveProduct(@RequestBody @Valid PreOfferProductRequest request,
                       @PathVariable("id") Integer id);

  @Operation(summary = "Add a product file associated with an PreOffer")
  @PostMapping("{id}/products/{productId}/files")
  @ResponseStatus(HttpStatus.OK)
  FileResponse addFile(
          @PathVariable("id") Integer id,
          @PathVariable("productId") Integer productId,
          @RequestParam("file") MultipartFile file,
          @RequestParam("extension") String extension
  );

  @Operation(summary = "Get all pre-offers")
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  PaginatedResponse<PreOfferResponse> findAll(PreOfferSpecification specification,
      @RequestParam(name = "page", defaultValue = "0") Integer page,
      @RequestParam(name = "size", defaultValue = "10") Integer size,
      @RequestParam(name = "sort-direction", defaultValue = "DESC") String direction,
      @RequestParam(name = "sort-attribute", defaultValue = "number") String attribute);

  @Operation(summary = "Get a PreOffer by its id")
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  PreOfferResponse findById(@PathVariable("id") Integer id);

  @Operation(summary = "Get a PreOffer by its number")
  @GetMapping(value = "/number/{number}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  List<PreOfferResponse> findByNumber(@PathVariable("number") Integer number);

  @Operation(summary = "Delete a PreOffer by its id")
  @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.ACCEPTED)
  void deleteById(@PathVariable("id") Integer id);

  @Operation(summary = "Delete a PreOffer by its number")
  @DeleteMapping(value = "/number/{number}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.ACCEPTED)
  void deleteByNumber(@PathVariable("number") Integer number);

  @Operation(summary = "Delete a product file from an PreOffer")
  @DeleteMapping(value = "/{id}/products/{productId}/files/{fileId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.ACCEPTED)
  void deleteByFileIdAndProductId(@PathVariable("id") Integer id, @PathVariable("productId") Integer productId,
                                  @PathVariable("fileId") Integer fileId);

  @Operation(summary = "Delete a product from an PreOffer")
  @DeleteMapping(value = "/{id}/products/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.ACCEPTED)
  void deleteByIdAndProductId(@PathVariable("id") Integer id,@PathVariable("productId") Integer productId);


  @Operation(summary = "Get a PreOffer pdf by its id")
  @PostMapping(value = "/{id}/pdf", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  Map<String, String> getPdfById(@PathVariable("id") Integer id) throws IOException;

  @Operation(summary = "Close a preOffer")
  @PostMapping(value = "/{id}/close")
  @ResponseBody
  @ResponseStatus(HttpStatus.ACCEPTED)
  void closeById(@PathVariable("id") Integer id);
}
