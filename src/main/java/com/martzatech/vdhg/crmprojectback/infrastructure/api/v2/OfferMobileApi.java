package com.martzatech.vdhg.crmprojectback.infrastructure.api.v2;

import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.OfferV2MobilSpecification;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.OfferMobileResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.PaginatedResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

public interface OfferMobileApi {
  @Operation(summary = "Get all offers")
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  PaginatedResponse<OfferMobileResponse> findAll(OfferV2MobilSpecification specification,
                                                 @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                 @RequestParam(name = "size", defaultValue = "10") Integer size,
                                                 @RequestParam(name = "sort-direction", defaultValue = "DESC") String direction,
                                                 @RequestParam(name = "sort-attribute", defaultValue = "number") String attribute,
                                                 @RequestParam(name = "active", defaultValue = "") Boolean active,
                                                 @RequestParam(name = "name", defaultValue = "") String name,
                                                 @RequestParam(name = "priceFrom", defaultValue = "") BigDecimal priceFrom,
                                                 @RequestParam(name = "priceTo", defaultValue = "") BigDecimal priceTo,
                                                 @RequestParam(name = "restricted", defaultValue = "") Boolean restricted,
                                                 @RequestHeader(value="Authorization") String token);

  @Operation(summary = "Get a Offer by its number")
  @GetMapping(value = "/number/{number}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  List<OfferMobileResponse> findByNumber(@PathVariable("number") Integer number,
                                         @RequestHeader(value="Authorization") String token);



  @Operation(summary = "Offer Send Email Mobile Offer")
  @PostMapping(value = "/send-chat/{id}",
          produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  boolean sendMobileChat(@PathVariable("id") Integer id,@RequestHeader(value="Authorization") String token);
}
