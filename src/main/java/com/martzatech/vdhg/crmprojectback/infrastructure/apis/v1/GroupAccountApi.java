package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1;

import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.GroupAccountSpecification;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.AssociatedRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.GroupAccountRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.AssociatedResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.GroupAccountResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.PaginatedResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
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

public interface GroupAccountApi {

  @Operation(summary = "Save a GroupAccount")
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  GroupAccountResponse save(@RequestBody @Valid GroupAccountRequest request);

  @Operation(summary = "Get all GroupAccounts")
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  PaginatedResponse<GroupAccountResponse> findAll(GroupAccountSpecification specification,
      @RequestParam(name = "page", defaultValue = "0") Integer page,
      @RequestParam(name = "size", defaultValue = "10") Integer size,
      @RequestParam(name = "sort-direction", defaultValue = "DESC") String direction,
      @RequestParam(name = "sort-attribute", defaultValue = "id") String attribute
  );

  @Operation(summary = "Get a GroupAccount by its id")
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  GroupAccountResponse findById(@PathVariable("id") Integer id);

  @Operation(summary = "Delete a GroupAccount by its id")
  @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.ACCEPTED)
  void deleteStatus(@PathVariable("id") Integer id);

  @Operation(summary = "Delete a GroupAccount by its id")
  @DeleteMapping(value = "/deleteById/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.ACCEPTED)
  void deleteById(@PathVariable("id") Integer id);

  @Operation(summary = "Save a Associated")
  @PostMapping(value = "/{groupAccountId}/associated", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  AssociatedResponse saveAssociated(@PathVariable("groupAccountId") Integer groupAccountId,
      @RequestBody @Valid AssociatedRequest request);

  @Operation(summary = "Delete a Associated by its id")
  @DeleteMapping(value = "/{groupAccountId}/associated/{associatedId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.ACCEPTED)
  void deleteAssociatedById(@PathVariable("groupAccountId") Integer groupAccountId,
      @PathVariable("associatedId") Integer associatedId);
}
