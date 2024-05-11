package com.martzatech.vdhg.crmprojectback.domains.customers.mappers;

import com.martzatech.vdhg.crmprojectback.domains.customers.entities.MembershipTypeEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.MembershipType;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MembershipTypeMapper {

  MembershipType entityToModel(MembershipTypeEntity entity);

  List<MembershipType> entitiesToModelList(List<MembershipTypeEntity> list);

  MembershipTypeEntity modelToEntity(MembershipType model);

  List<MembershipTypeEntity> modelsToEntityList(List<MembershipType> list);
}
