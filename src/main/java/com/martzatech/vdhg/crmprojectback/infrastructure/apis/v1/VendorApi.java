package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1;

import com.martzatech.vdhg.crmprojectback.domains.vendors.specifications.VendorSpecification;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.NoteRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.VendorRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.FileResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.NoteResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.PaginatedResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.VendorResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

public interface VendorApi {
    @Operation(summary = "Save a Vendor")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    VendorResponse save(@RequestBody @Valid VendorRequest request);

    @Operation(summary = "Add File of a vendor")
    @PostMapping("{id}/file")
    @ResponseStatus(HttpStatus.OK)
    FileResponse addFile(
            @PathVariable("id") Integer id,
            @RequestParam("file") MultipartFile file,
            @RequestParam("extension") String extension
    );
    @Operation(summary = "Get all Vendors")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    PaginatedResponse<VendorResponse> findAll(VendorSpecification specification,
                                              @RequestParam(name = "page", defaultValue = "0") Integer page,
                                              @RequestParam(name = "size", defaultValue = "10") Integer size,
                                              @RequestParam(name = "sort-direction", defaultValue = "DESC") String direction,
                                              @RequestParam(name = "sort-attribute", defaultValue = "id") String attribute
    );
    @Operation(summary = "Get a Vendor by its id")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    VendorResponse findById(@PathVariable("id") Integer id);
    @Operation(summary = "Delete a Vendor by its id Hardt delete")
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    void deleteById(@PathVariable("id") Integer id);

    @Operation(summary = "Create note to vendor")
    @PostMapping(value = "/{id}/notes", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    NoteResponse saveNote(@PathVariable("id") Integer id, @RequestBody @Valid NoteRequest note);
}
