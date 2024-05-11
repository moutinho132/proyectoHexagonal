package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.commons.models.AttributeType;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.AttributeTypeRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.AttributeTypeResponse;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AttributeTypeApiMapper {

  AttributeType requestToModel(AttributeTypeRequest request);

  List<AttributeType> requestToModelList(List<AttributeTypeRequest> list);

  AttributeTypeResponse modelToResponse(AttributeType model);

  List<AttributeTypeResponse> modelsToResponseList(List<AttributeType> list);
}
