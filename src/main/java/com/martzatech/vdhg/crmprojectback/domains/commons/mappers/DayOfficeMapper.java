package com.martzatech.vdhg.crmprojectback.domains.commons.mappers;

import com.martzatech.vdhg.crmprojectback.domains.commons.entities.DayOfficeEntity;
import com.martzatech.vdhg.crmprojectback.domains.commons.models.DayOffice;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DayOfficeMapper {

    DayOffice entityToModel(DayOfficeEntity entity);

    List<DayOffice> entitiesToModelList(List<DayOfficeEntity> list);

    DayOfficeEntity modelToEntity(DayOffice model);

    List<DayOfficeEntity> modelsToEntityList(List<DayOffice> list);
}
