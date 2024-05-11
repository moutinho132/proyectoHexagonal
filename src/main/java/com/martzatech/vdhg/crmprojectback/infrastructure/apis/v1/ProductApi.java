package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1;

import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.ProductSpecification;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.NoteRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.ProductRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.NoteResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.PaginatedResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.ProductFileResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.ProductResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

public interface ProductApi {

  @Operation(summary = "Save a Product")
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  ProductResponse save(@RequestBody ProductRequest request);

  @Operation(summary = "Add picture of a product")
  @PostMapping("{id}/picture")
  @ResponseStatus(HttpStatus.OK)
  ProductFileResponse addPicture(
      @PathVariable("id") Integer id,
      @RequestParam("file") MultipartFile file,
      @RequestParam("extension") String extension
  );

  @Operation(summary = "Get all Products")
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  PaginatedResponse<ProductResponse> findAll(ProductSpecification specification,
      @RequestParam(name = "page", defaultValue = "0") Integer page,
      @RequestParam(name = "size", defaultValue = "10") Integer size,
      @RequestParam(name = "sort-direction", defaultValue = "DESC") String direction,
      @RequestParam(name = "sort-attribute", defaultValue = "id") String attribute);

  @Operation(summary = "Get a Product by its id")
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  ProductResponse findById(@PathVariable("id") Integer id);

  @Operation(summary = "Delete a Product by its id soft delete")
  @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.ACCEPTED)
  void deleteById(@PathVariable("id") Integer id);

  @Operation(summary = "Delete a Product by its id hard delete")
  @DeleteMapping(value = "/deleteByStatusAndId/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.ACCEPTED)
  void deleteByStatusAndId(@PathVariable("id") Integer id);

  @Operation(summary = "Add picture of a product")
  @DeleteMapping("{id}/picture/{fileId}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  void deletePicture(
      @PathVariable("id") Integer id,
      @PathVariable("fileId") Integer fileId
  );

  @Operation(summary = "Create note to product")
  @PostMapping(value = "/{id}/notes", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  NoteResponse saveNote(@PathVariable("id") Integer id, @RequestBody @Valid NoteRequest note);
}
