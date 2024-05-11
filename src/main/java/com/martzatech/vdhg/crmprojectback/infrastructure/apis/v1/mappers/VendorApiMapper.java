package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.vendors.models.Vendor;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.VendorRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.VendorResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring")
public interface VendorApiMapper {

    Vendor requestToModel(VendorRequest request);

    List<Vendor> requestToModelList(List<VendorRequest> list);

    VendorResponse modelToResponse(Vendor model);

    List<VendorResponse> modelsToResponseList(List<Vendor> list);
}
