package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.security.models.Department;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.DepartmentRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.DepartmentResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                SubsidiaryApiMapper.class,
                RoleApiMapper.class
        }
)
public interface DepartmentApiMapper {

    Department requestToModel(DepartmentRequest request);

    List<Department> requestToModelList(List<DepartmentRequest> list);

    DepartmentResponse modelToResponse(Department model);

    List<DepartmentResponse> modelsToResponseList(List<Department> list);
}
