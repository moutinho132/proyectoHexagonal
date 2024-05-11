package com.martzatech.vdhg.crmprojectback.infrastructure.api.v2;

import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.PreOfferV2MobilSpecification;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.PaginatedResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.PreOfferResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

public interface PreOfferMobileApi {

    @Operation(summary = "Get all pre-offers")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    PaginatedResponse<PreOfferResponse> findAll(PreOfferV2MobilSpecification specification,
                                                @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                @RequestParam(name = "size", defaultValue = "10") Integer size,
                                                @RequestParam(name = "sort-direction", defaultValue = "DESC") String direction,
                                                @RequestParam(name = "sort-attribute", defaultValue = "number") String attribute,
                                                @RequestHeader(value="Authorization") String token);

    @Operation(summary = "PreOffer Send Email Mobile")
    @PostMapping(value = "/send-chat/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    boolean sendMobileChat(@PathVariable("id") Integer id,@RequestHeader(value="Authorization") String token);

}
