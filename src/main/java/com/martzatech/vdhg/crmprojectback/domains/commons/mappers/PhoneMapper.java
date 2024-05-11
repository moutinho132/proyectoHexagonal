package com.martzatech.vdhg.crmprojectback.domains.commons.mappers;

import com.martzatech.vdhg.crmprojectback.domains.commons.entities.PhoneEntity;
import com.martzatech.vdhg.crmprojectback.domains.commons.models.Phone;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = {
        AttributeTypeMapper.class
    }
)
public interface PhoneMapper {

  Phone entityToModel(PhoneEntity entity);

  List<Phone> entitiesToModelList(List<PhoneEntity> list);

  PhoneEntity modelToEntity(Phone model);

  List<PhoneEntity> modelsToEntityList(List<Phone> list);
}
