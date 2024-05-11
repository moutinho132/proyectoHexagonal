package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.commons.models.Phone;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.PhoneRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.PhoneResponse;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = {
        AttributeTypeApiMapper.class
    }
)
public interface PhoneApiMapper {

  Phone requestToModel(PhoneRequest request);

  List<Phone> requestToModelList(List<PhoneRequest> list);

  PhoneResponse modelToResponse(Phone model);

  List<PhoneResponse> modelsToResponseList(List<Phone> list);
}
