package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.commons.models.IdentityDocument;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.IdentityDocumentRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.IdentityDocumentResponse;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = {
        IdentityDocumentTypeApiMapper.class
    }
)
public interface IdentityDocumentApiMapper {

  IdentityDocument requestToModel(IdentityDocumentRequest request);

  List<IdentityDocument> requestToModelList(List<IdentityDocumentRequest> list);

  IdentityDocumentResponse modelToResponse(IdentityDocument model);

  List<IdentityDocumentResponse> modelsToResponseList(List<IdentityDocument> list);
}
