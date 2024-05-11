package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.commons.models.PersonTitle;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.PersonTitleRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.PersonTitleResponse;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonTitleApiMapper {

  PersonTitle requestToModel(PersonTitleRequest request);

  List<PersonTitle> requestToModelList(List<PersonTitleRequest> list);

  PersonTitleResponse modelToResponse(PersonTitle model);

  List<PersonTitleResponse> modelsToResponseList(List<PersonTitle> list);
}
