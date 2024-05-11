package com.martzatech.vdhg.crmprojectback.domains.commons.mappers;

import com.martzatech.vdhg.crmprojectback.domains.commons.entities.AddressEntity;
import com.martzatech.vdhg.crmprojectback.domains.commons.models.Address;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = {
        AttributeTypeMapper.class
    }
)
public interface AddressMapper {

  Address entityToModel(AddressEntity entity);

  List<Address> entitiesToModelList(List<AddressEntity> list);

  AddressEntity modelToEntity(Address model);

  List<AddressEntity> modelsToEntityList(List<Address> list);
}
