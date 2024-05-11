package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.security.models.Privilege;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.PrivilegeResponse;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = PermissionApiMapper.class
)
public interface PrivilegeApiMapper {

  PrivilegeResponse modelToResponse(Privilege model);

  List<PrivilegeResponse> modelsToResponseList(List<Privilege> list);
}
