package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.security.models.Permission;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.PermissionRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.PermissionResponse;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionApiMapper {

  Permission requestToModel(PermissionRequest request);

  List<Permission> requestToModelList(List<PermissionRequest> list);

  PermissionResponse modelToResponse(Permission model);

  List<PermissionResponse> modelsToResponseList(List<Permission> list);
}
