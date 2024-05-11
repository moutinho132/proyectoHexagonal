package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.security.models.AccessGroup;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.AccessGroupResponse;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = {
        AccessApiMapper.class
    }
)
public interface AccessGroupApiMapper {

  AccessGroupResponse modelToResponse(AccessGroup model);

  List<AccessGroupResponse> modelsToResponseList(List<AccessGroup> list);
}
