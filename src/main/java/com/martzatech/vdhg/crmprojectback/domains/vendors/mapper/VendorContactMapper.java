package com.martzatech.vdhg.crmprojectback.domains.vendors.mapper;

import com.martzatech.vdhg.crmprojectback.domains.vendors.entities.VendorContactEntity;
import com.martzatech.vdhg.crmprojectback.domains.vendors.models.VendorContact;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring"
)
public interface VendorContactMapper {
    VendorContact entityToModel(VendorContactEntity entity);

    List<VendorContact> entitiesToModelList(List<VendorContactEntity> list);

    VendorContactEntity modelToEntity(VendorContact model);

    List<VendorContactEntity> modelsToEntityList(List<VendorContact> list);
}
