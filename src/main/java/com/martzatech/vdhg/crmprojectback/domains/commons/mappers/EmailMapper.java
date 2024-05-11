package com.martzatech.vdhg.crmprojectback.domains.commons.mappers;

import com.martzatech.vdhg.crmprojectback.domains.commons.entities.EmailEntity;
import com.martzatech.vdhg.crmprojectback.domains.commons.models.Email;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = {
        AttributeTypeMapper.class
    }
)
public interface EmailMapper {

  Email entityToModel(EmailEntity entity);

  List<Email> entitiesToModelList(List<EmailEntity> list);

  EmailEntity modelToEntity(Email model);

  List<EmailEntity> modelsToEntityList(List<Email> list);
}
