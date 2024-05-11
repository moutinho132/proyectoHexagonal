package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.security.models.Access;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.AccessResponse;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = {
        PrivilegeApiMapper.class
    }
)
public interface AccessApiMapper {

  AccessResponse modelToResponse(Access model);

  List<AccessResponse> modelsToResponseList(List<Access> list);
}
