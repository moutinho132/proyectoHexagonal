package com.martzatech.vdhg.crmprojectback.domains.customers.mappers;

import com.martzatech.vdhg.crmprojectback.domains.customers.entities.AssociatedEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Associated;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                CustomNamedMappers.class,
                GroupAccountMapper.class,
        }
)
public interface AssociatedMapper {

    @Mapping(source = "creationUser", target = "creationUser", qualifiedByName = "customUser")
    @Mapping(source = "modificationUser", target = "modificationUser", qualifiedByName = "customUser")
    @Mapping(source = "groupAccount", target = "groupAccount", qualifiedByName = "customGroupAccountModel")
    Associated entityToModel(AssociatedEntity entity);


    @Mapping(source = "creationUser", target = "creationUser", qualifiedByName = "customUser")
    @Mapping(source = "modificationUser", target = "modificationUser", qualifiedByName = "customUser")
    List<Associated> entitiesToModelList(List<AssociatedEntity> list);

    @Mapping(source = "groupAccount", target = "groupAccount", qualifiedByName = "customGroupAccount")
    AssociatedEntity modelToEntity(Associated model);

    List<AssociatedEntity> modelsToEntityList(List<Associated> list);
}
