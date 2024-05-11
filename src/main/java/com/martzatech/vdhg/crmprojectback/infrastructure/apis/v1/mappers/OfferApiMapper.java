package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.customers.mappers.BockingProductMapper;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Customer;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Offer;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.OfferRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Objects;

@Mapper(
        componentModel = "spring",
        uses = {
                DiscountApiMapper.class,
                BockingProductApiMapper.class,
                PreOfferApiMapper.class,
                CustomerApiMapper.class,
                UserApiMapper.class,
                BockingProductMapper.class,
        }
)
public interface OfferApiMapper {

    Offer requestToModel(OfferRequest request);

    List<Offer> requestToModelList(List<OfferRequest> list);
    @Mapping(source = "files", target = "files", ignore = true)
    //@Mapping(source = "customer", target = "customer", qualifiedByName = "customerResponseMapping")
    OfferResponse modelToResponse(Offer model);

    List<OfferResponse> modelsToResponseList(List<Offer> list);

    List<OfferMobileResponse> modelsToResponseMobileList(List<Offer> list);

    @Named("customerResponseMapping")
    default CustomerResponse customerResponseMapping(final Customer model) {
        return Objects.nonNull(model)
                ? CustomerResponse.builder()
                .id(model.getId())
                .creationTime(Objects.nonNull(model.getCreationTime()) ? model.getCreationTime() : null)
                .creationUser(Objects.nonNull(model.getCreationUser()) ? UserResponse.builder().id(model.getCreationUser().getId()).build() : null)
                .modificationTime(Objects.nonNull(model.getModificationTime()) ? model.getModificationTime() : null)
                .modificationUser(Objects.nonNull(model.getModificationUser()) ? UserResponse.builder().id(model.getModificationUser().getId()).build() : null)
                .person(Objects.nonNull(model.getPerson()) ? PersonResponse.builder()
                        .id(model.getPerson().getId())
                        .name(model.getPerson().getName())
                        .surname(model.getPerson().getSurname())
                        .build() : null)
                .build() : null;
    }
}
