package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.customers.models.PreOffer;
import com.martzatech.vdhg.crmprojectback.domains.security.entities.UserEntity;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.PreOfferRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.PreOfferResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.PreOfferResponseNew;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;
import java.util.Objects;

@Mapper(
        componentModel = "spring",
        uses = {
                DiscountApiMapper.class,
                BockingProductApiMapper.class,
                UserApiMapper.class,
        }
)
public interface PreOfferApiMapper {

    PreOffer requestToModel(PreOfferRequest request);

    List<PreOffer> requestToModelList(List<PreOfferRequest> list);
    PreOfferResponse modelToResponse(PreOffer model);

    PreOfferResponseNew modelToResponseNew(PreOffer model);

    @Named("customUser")
    static User customUser(final UserEntity entity) {
        return Objects.nonNull(entity)
                ? User
                .builder()
                .id(Objects.nonNull(entity.getId()) ? entity.getId() : null)
                .title(Objects.nonNull(entity.getTitle()) ? entity.getTitle() : null)
                .name(Objects.nonNull(entity.getName()) ? entity.getName() : null)
                .surname(Objects.nonNull(entity.getSurname()) ? entity.getSurname() : null)
                .mobile(Objects.nonNull(entity.getMobile()) ? entity.getMobile() : null)
                .nationality(Objects.nonNull(entity.getNationality()) ? entity.getNationality() : null)
                .address(Objects.nonNull(entity.getAddress()) ? entity.getAddress() : null)
                .email(Objects.nonNull(entity.getEmail()) ? entity.getEmail() : null)
                .creationTime(Objects.nonNull(entity.getCreationTime()) ? entity.getCreationTime() : null)
                .modificationTime(Objects.nonNull(entity.getModificationTime()) ? entity.getModificationTime() : null)
                .build()
                : null;
    }
    List<PreOfferResponse> modelsToResponseList(List<PreOffer> list);

  //  List<ProductOfferBokingsResponse> modelsToResponseList(List<PreOffer> list);

}
