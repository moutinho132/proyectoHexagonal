package com.martzatech.vdhg.crmprojectback.domains.customers.mappers;

import com.martzatech.vdhg.crmprojectback.domains.customers.entities.MembershipEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Membership;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = {
        MembershipTypeMapper.class
    }
)
public interface MembershipMapper {

  Membership entityToModel(MembershipEntity entity);

  List<Membership> entitiesToModelList(List<MembershipEntity> list);

  MembershipEntity modelToEntity(Membership model);

  List<MembershipEntity> modelsToEntityList(List<Membership> list);
}
