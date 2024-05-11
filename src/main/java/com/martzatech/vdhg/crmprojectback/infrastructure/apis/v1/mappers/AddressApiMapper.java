package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.commons.models.Address;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.AddressRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.AddressResponse;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = {
        CountryApiMapper.class,
        AttributeTypeApiMapper.class
    }
)
public interface AddressApiMapper {

  Address requestToModel(AddressRequest request);

  List<Address> requestToModelList(List<AddressRequest> list);

  AddressResponse modelToResponse(Address model);

  List<AddressResponse> modelsToResponseList(List<Address> list);
}
