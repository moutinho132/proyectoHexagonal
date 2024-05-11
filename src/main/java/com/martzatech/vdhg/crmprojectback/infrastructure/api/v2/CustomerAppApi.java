package com.martzatech.vdhg.crmprojectback.infrastructure.api.v2;

import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.WalletSpecification;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.FileWalletResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.PaginatedResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

public interface CustomerAppApi {

  /*@Operation(summary = "Get all Customers")
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  PaginatedResponse<CustomerResponse> findAll(CustomerSpecification specification,
      @RequestParam(name = "page", defaultValue = "0") Integer page,
      @RequestParam(name = "size", defaultValue = "10") Integer size,
      @RequestParam(name = "sort-direction", defaultValue = "DESC") String direction,
      @RequestParam(name = "sort-attribute", defaultValue = "id") String attribute,
      @RequestHeader(value="Authorization") String token);*/

    @Operation(summary = "Get a Customer by its id")
    @GetMapping(value = "/{id}/wallet", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    PaginatedResponse<FileWalletResponse> findByWalletCustomer(@PathVariable("id") Integer id, final WalletSpecification specification,
                                                               @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                               @RequestParam(name = "size", defaultValue = "10") Integer size,
                                                               @RequestParam(name = "sort-direction", defaultValue = "DESC") String direction,
                                                               @RequestParam(name = "sort-attribute", defaultValue = "id") String attribute,
                                                               @RequestHeader(value="Authorization") String token);

}
