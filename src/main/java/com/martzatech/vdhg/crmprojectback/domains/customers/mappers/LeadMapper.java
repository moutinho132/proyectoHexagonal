package com.martzatech.vdhg.crmprojectback.domains.customers.mappers;

import com.martzatech.vdhg.crmprojectback.domains.commons.mappers.PersonMapper;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.CompanyEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.LeadEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Company;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Lead;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Objects;

@Mapper(
        componentModel = "spring",
        uses = {
                LeadStatusMapper.class,
                CompanyMapper.class,
                RegistrationTypeMapper.class,
                PersonMapper.class
        }
)
public interface LeadMapper {

    @Mapping(source = "referringCustomer", target = "referringCustomer", ignore = true)
    Lead entityToModel(LeadEntity entity);

    List<Lead> entitiesToModelList(List<LeadEntity> list);

    @Mapping(source = "referringCustomer", target = "referringCustomer", ignore = true)
    @Mapping(source = "company", target = "company", qualifiedByName = "customCompany")
    LeadEntity modelToEntity(Lead model);

    List<LeadEntity> modelsToEntityList(List<Lead> list);

    @Named("customCompany")
    static CompanyEntity customCompany(final Company model) {
        return Objects.nonNull(model)
                ? CompanyEntity.builder()
                .id(model.getId())
                .name(model.getName())
                .build()
                : null;
    }
}
