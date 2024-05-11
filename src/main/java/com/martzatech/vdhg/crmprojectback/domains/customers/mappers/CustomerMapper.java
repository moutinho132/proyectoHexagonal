package com.martzatech.vdhg.crmprojectback.domains.customers.mappers;

import com.martzatech.vdhg.crmprojectback.domains.commons.mappers.PersonMapper;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.CustomerEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.GroupAccountEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Customer;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.GroupAccount;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Membership;
import com.martzatech.vdhg.crmprojectback.domains.security.mappers.UserMapper;
import io.micrometer.common.util.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Objects;

@Mapper(
        componentModel = "spring",
        uses = {
                MembershipMapper.class,
                CustomerStatusMapper.class,
                CompanyMapper.class,
                PersonMapper.class,
                LeadMapper.class,
                CustomerStatusMapper.class,
                CreationTypeMapper.class,
                UserMapper.class,
                PaymentDetailsMapper.class,
                CustomNamedMappers.class,
                GroupAccountMapper.class,
        }
)
public interface CustomerMapper {

    @Mapping(source = "creationUser", target = "creationUser", qualifiedByName = "customUser")
    @Mapping(source = "modificationUser", target = "modificationUser", qualifiedByName = "customUser")
    //@Mapping(source = "groupAccount", target = "groupAccount", ignore = true)
    @Mapping(source = "groupAccount", target = "groupAccount", qualifiedByName = "mapperGroupAccountToModel")
    Customer entityToModel(CustomerEntity entity);

    List<Customer> entitiesToModelList(List<CustomerEntity> list);

    @Mapping(target = "groupAccount", ignore = true)
    CustomerEntity modelToEntity(Customer model);

    List<CustomerEntity> modelsToEntityList(List<Customer> list);

    @Named("mapperGroupAccountToModel")
    default GroupAccount mapperGroupAccountToModel(final GroupAccountEntity entity) {
        if (Objects.nonNull(entity)) {
            return GroupAccount.builder()
                    .id(entity.getId())
                    .name(entity.getName())
                    .industry(entity.getIndustry())
                    .vat(entity.getVat())
                    .email(entity.getEmail())
                    .owner(Objects.nonNull(entity.getOwner())? Customer
                            .builder()
                            .id(entity.getOwner().getId())
                            .membership(Objects.nonNull(entity.getOwner().getMembership())?
                                    Membership.builder()
                                            .id(entity.getOwner().getMembership().getId())
                                            .name(StringUtils.isNotEmpty(entity.getOwner().getMembership().getName())?entity.getOwner().getMembership().getName():null)
                                            .priority(entity.getOwner().getMembership().getPriority())
                                            .price(entity.getOwner().getMembership().getPrice()).build():null)
                            .build() : null)
                    .alias(entity.getAlias())
                    .creationTime(entity.getCreationTime())
                    .creationUser(CustomNamedMappers.customDepartmentMapping(entity.getCreationUser()))
                    .modificationTime(entity.getModificationTime())
                    .modificationUser(CustomNamedMappers.customDepartmentMapping(entity.getModificationUser()))
                    .build();
        }
        return null;
    }
}
