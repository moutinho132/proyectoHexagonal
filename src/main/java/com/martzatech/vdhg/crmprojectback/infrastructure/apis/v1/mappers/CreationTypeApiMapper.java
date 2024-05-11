package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.customers.models.CreationType;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.CreationTypeResponse;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CreationTypeApiMapper {

  CreationTypeResponse modelToResponse(CreationType model);

  List<CreationTypeResponse> modelsToResponseList(List<CreationType> list);
}
