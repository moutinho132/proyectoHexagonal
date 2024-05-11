package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1;

import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.OrderSpecification;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.NoteRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.OrderRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.NoteResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.OrderResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.PaginatedResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.StatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface OrderApi {

  @Operation(summary = "Save a Order")
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  OrderResponse save(@RequestBody @Valid OrderRequest request) throws IOException;

  @Operation(summary = "Order Send")
  @PostMapping(value = "/{id}/send",consumes = MediaType.APPLICATION_JSON_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  boolean sendEmailAndChat(@PathVariable("id") Integer id);

  @Operation(summary = "Change Order status")
  @PostMapping(path = "/{id}/status/{statusId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.ACCEPTED)
  void changeStatus(@PathVariable("id") Integer id, @PathVariable("statusId") Integer statusId);

  @Operation(summary = "Get all order status")
  @GetMapping(path = "/status", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  List<StatusResponse> getStatus();

  @Operation(summary = "Get all Orders")
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  PaginatedResponse<OrderResponse> findAll(OrderSpecification specification,
      @RequestParam(name = "page", defaultValue = "0") Integer page,
      @RequestParam(name = "size", defaultValue = "10") Integer size,
      @RequestParam(name = "sort-direction", defaultValue = "DESC") String direction,
      @RequestParam(name = "sort-attribute", defaultValue = "id") String attribute);

  @Operation(summary = "Get a Order by its id")
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  OrderResponse findById(@PathVariable("id") Integer id);

  @Operation(summary = "Delete a Order by its id soft delete")
  @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.ACCEPTED)
  void deleteByStatus(@PathVariable("id") Integer id);
  @Operation(summary = "Delete a Order by its id")
  @DeleteMapping(value = "/deleteById/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.ACCEPTED)
  void deleteById(@PathVariable("id") Integer id);

    @Operation(summary = "Get a Order pdf by its id")
    @PostMapping(value = "/{id}/pdf", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    Map<String, String> getPdfById(@PathVariable("id") Integer id) throws IOException;

  @Operation(summary = "Get a Order pdf by its id")
  @PostMapping(value = "/{id}/pdf/new", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  Map<String, String> getPdfNewById(@PathVariable("id") Integer id) throws IOException;

  @Operation(summary = "Create note to order")
  @PostMapping(value = "/{id}/notes", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  NoteResponse saveNote(@PathVariable("id") Integer id, @RequestBody @Valid NoteRequest note);
}
