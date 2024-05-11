package com.martzatech.vdhg.crmprojectback.infrastructure.api.v2;

import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.OrderMobileSpecification;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.OrderRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.OrderMobilResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.OrderMobileResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.OrderResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.PaginatedResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;

public interface OrderMobileApi {
    @Operation(summary = "Save a Order")
    @PostMapping(value =  "/generate",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    OrderMobileResponse save(@RequestBody @Valid OrderRequest request, @RequestHeader(value = "Authorization") String token) throws IOException;



    @Operation(summary = "Get all Orders Mobile")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    PaginatedResponse<OrderMobilResponse> findAll(OrderMobileSpecification specification,
                                                  @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                  @RequestParam(name = "size", defaultValue = "10") Integer size,
                                                  @RequestParam(name = "sort-direction", defaultValue = "DESC") String direction,
                                                  @RequestParam(name = "sort-attribute", defaultValue = "id") String attribute,
                                                  @RequestParam(name = "number", defaultValue = "") Integer number,
                                                  @RequestParam(name = "priceForm", defaultValue = "") BigDecimal priceForm,
                                                  @RequestParam(name = "priceTo", defaultValue = "") BigDecimal priceTo,
                                                  @RequestParam(name = "status", defaultValue = "") String status,
                                                  @RequestHeader(value = "Authorization") String token);
    @Operation(summary = "Get a Order by its id Mobile")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    OrderResponse findById(@PathVariable("id") Integer id,
                           @RequestHeader(value = "Authorization") String token);

    @Operation(summary = "Order Send Email Mobile Order")
    @PostMapping(value = "/send-chat/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    boolean sendMobileChat(@PathVariable("id") Integer id,@RequestHeader(value="Authorization") String token);
}
