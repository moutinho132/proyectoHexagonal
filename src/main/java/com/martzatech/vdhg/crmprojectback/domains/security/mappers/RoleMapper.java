package com.martzatech.vdhg.crmprojectback.domains.security.mappers;

import com.martzatech.vdhg.crmprojectback.domains.security.entities.DepartmentEntity;
import com.martzatech.vdhg.crmprojectback.domains.security.entities.RoleEntity;
import com.martzatech.vdhg.crmprojectback.domains.security.models.Department;
import com.martzatech.vdhg.crmprojectback.domains.security.models.Role;
import java.util.List;
import java.util.Objects;

import com.martzatech.vdhg.crmprojectback.domains.security.models.Subsidiary;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(
    componentModel = "spring",
    uses = {
        PrivilegeMapper.class,
        CommonNamed.class,
    }
)
public interface RoleMapper {

  @Mapping(source = "department", target = "department", qualifiedByName = "customDepartmentMapping")
  @Mapping(source = "creationUser", target = "creationUser", qualifiedByName = "customUserMapping")
  @Mapping(source = "modificationUser", target = "modificationUser", qualifiedByName = "customUserMapping")
  Role entityToModel(RoleEntity entity);


  @Mapping(source = "status", target = "status")
  List<Role> entitiesToModelList(List<RoleEntity> list);

  RoleEntity modelToEntity(Role model);

  List<RoleEntity> modelsToEntityList(List<Role> list);

  @Named("customDepartmentMapping")
  static Department customDepartmentMapping(final DepartmentEntity entity) {
    return Objects.nonNull(entity)
        ? Department
        .builder()
        .id(entity.getId())
        .name(entity.getName())
        .description(entity.getDescription())
        .creationTime(entity.getCreationTime())
        .modificationTime(entity.getModificationTime())
            .subsidiary(Objects.nonNull(entity.getSubsidiary())?
                    Subsidiary.builder().id(entity.getSubsidiary().getId()).build():null)
        .build()
        : null;
  }
}
