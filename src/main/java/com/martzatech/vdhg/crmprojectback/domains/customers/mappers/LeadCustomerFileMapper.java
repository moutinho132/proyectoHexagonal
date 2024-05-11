package com.martzatech.vdhg.crmprojectback.domains.customers.mappers;

import com.martzatech.vdhg.crmprojectback.domains.customers.entities.AssociatedEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.CustomerEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.LeadCustomerFileEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.MembershipEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.*;
import com.martzatech.vdhg.crmprojectback.domains.security.entities.UserEntity;
import com.martzatech.vdhg.crmprojectback.domains.security.mappers.CommonNamed;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Objects;

@Mapper(
    componentModel = "spring",
    uses = {
        CommonNamed.class
    }
)
public interface LeadCustomerFileMapper {

  @Mapping(source = "creationUser", target = "creationUser", qualifiedByName = "customUserMapping")
  @Mapping(source = "removalUser", target = "removalUser", qualifiedByName = "customUserMapping")
  LeadCustomerFile entityToModel(LeadCustomerFileEntity entity);

  List<LeadCustomerFile> entitiesToModelList(List<LeadCustomerFileEntity> list);


  @Mapping(source = "creationUser", target = "creationUser", qualifiedByName = "customUserMapping")
  @Mapping(source = "removalUser", target = "removalUser", qualifiedByName = "customUserMapping")
  @Mapping(source = "customer", target = "customer", qualifiedByName = "customerModelToEntityMapping")
  LeadCustomerFileEntity modelToEntity(LeadCustomerFile model);

  List<LeadCustomerFileEntity> modelsToEntityList(List<LeadCustomerFile> list);

  @Named("customUserMapping")
  static UserEntity customUserMapping(final User model) {
    return Objects.nonNull(model)
            ? UserEntity
            .builder()
            .id(model.getId())
            .title(Objects.nonNull(model.getTitle()) ? model.getTitle() : null)
            .name(model.getName())
            .surname(model.getSurname())
            .email(model.getEmail())
            .address(model.getAddress())
            .mobile(model.getMobile())
            .customer(Objects.nonNull(model.getCustomer()) ? CustomerEntity.builder()
                    .id(model.getCustomer().getId())
                    .membership(MembershipEntity.builder()
                            .id(model.getCustomer().getMembership().getId())
                            .priority(model.getCustomer().getMembership().getPriority()).build())
                    .build() : null)
            .associated(Objects.nonNull(model.getAssociated())? AssociatedEntity.builder().id(model.getAssociated().getId()).build():null)
            .typeUser(Objects.nonNull(model.getTypeUser()) ? model.getTypeUser() : null)
            .build()
            : null;
  }
}
