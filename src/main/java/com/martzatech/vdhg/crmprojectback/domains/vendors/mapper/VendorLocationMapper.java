package com.martzatech.vdhg.crmprojectback.domains.vendors.mapper;

import com.martzatech.vdhg.crmprojectback.domains.vendors.entities.VendorLocationEntity;
import com.martzatech.vdhg.crmprojectback.domains.vendors.models.VendorLocation;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring"
)
public interface VendorLocationMapper {
    VendorLocation entityToModel(VendorLocationEntity entity);

    List<VendorLocation> entitiesToModelList(List<VendorLocationEntity> list);

    VendorLocationEntity modelToEntity(VendorLocation model);

    List<VendorLocationEntity> modelsToEntityList(List<VendorLocation> list);
}
