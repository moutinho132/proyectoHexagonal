package com.martzatech.vdhg.crmprojectback.domains.customers.mappers;

import com.martzatech.vdhg.crmprojectback.domains.customers.entities.GroupAccountEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.GroupAccount;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = {
        CustomNamedMappers.class,
        CustomerMapper.class,
        AssociatedMapper.class,
    }
)
public interface GroupAccountMapper {

  @Mapping(source = "creationUser", target = "creationUser", qualifiedByName = "customUser")
  @Mapping(source = "modificationUser", target = "modificationUser", qualifiedByName = "customUser")
  @Mapping(source = "associates", target = "associates", qualifiedByName = "customAssociatesRevers")
  GroupAccount entityToModel(GroupAccountEntity entity);
  @Mapping(source = "associates", target = "associates", qualifiedByName = "customAssociates")
  List<GroupAccount> entitiesToModelList(List<GroupAccountEntity> list);
  @Mapping(source = "associates", target = "associates", qualifiedByName = "customAssociates")
  GroupAccountEntity modelToEntity(GroupAccount model);

  List<GroupAccountEntity> modelsToEntityList(List<GroupAccount> list);
}
