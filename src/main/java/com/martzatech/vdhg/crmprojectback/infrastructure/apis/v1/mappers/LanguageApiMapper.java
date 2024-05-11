package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.commons.models.Language;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.LanguageRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.LanguageResponse;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LanguageApiMapper {

  Language requestToModel(LanguageRequest request);

  List<Language> requestToModelList(List<LanguageRequest> list);

  LanguageResponse modelToResponse(Language model);

  List<LanguageResponse> modelsToResponseList(List<Language> list);
}
