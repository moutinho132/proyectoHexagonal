package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.security.models.Role;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.RoleRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.RoleResponse;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = {
        PermissionApiMapper.class
    }
)
public interface RoleApiMapper {

  Role requestToModel(RoleRequest request);

  List<Role> requestToModelList(List<RoleRequest> list);

  RoleResponse modelToResponse(Role model);

  List<RoleResponse> modelsToResponseList(List<Role> list);
}
