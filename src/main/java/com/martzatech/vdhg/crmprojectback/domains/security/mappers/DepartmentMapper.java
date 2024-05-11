package com.martzatech.vdhg.crmprojectback.domains.security.mappers;

import com.martzatech.vdhg.crmprojectback.domains.security.entities.DepartmentEntity;
import com.martzatech.vdhg.crmprojectback.domains.security.models.Department;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = {
        SubsidiaryMapper.class,
        RoleMapper.class,
        CommonNamed.class,
    }
)
public interface DepartmentMapper {

  @Mapping(source = "creationUser", target = "creationUser", qualifiedByName = "customUserMapping")
  @Mapping(source = "modificationUser", target = "modificationUser", qualifiedByName = "customUserMapping")
  @Mapping(source = "status", target = "status")
  Department entityToModel(DepartmentEntity entity);

  List<Department> entitiesToModelList(List<DepartmentEntity> list);

  DepartmentEntity modelToEntity(Department model);

  List<DepartmentEntity> modelsToEntityList(List<Department> list);
}
