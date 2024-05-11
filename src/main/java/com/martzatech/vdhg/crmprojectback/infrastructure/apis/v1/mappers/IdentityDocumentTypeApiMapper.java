package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.commons.models.IdentityDocumentType;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.IdentityDocumentTypeRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.IdentityDocumentTypeResponse;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IdentityDocumentTypeApiMapper {

  IdentityDocumentType requestToModel(IdentityDocumentTypeRequest request);

  List<IdentityDocumentType> requestToModelList(List<IdentityDocumentTypeRequest> list);

  IdentityDocumentTypeResponse modelToResponse(IdentityDocumentType model);

  List<IdentityDocumentTypeResponse> modelsToResponseList(List<IdentityDocumentType> list);
}
