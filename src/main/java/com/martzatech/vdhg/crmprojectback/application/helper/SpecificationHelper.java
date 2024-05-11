package com.martzatech.vdhg.crmprojectback.application.helper;

import com.martzatech.vdhg.crmprojectback.application.enums.DeletedStatusEnum;
import com.martzatech.vdhg.crmprojectback.domains.chat.entities.ChatRoomEntity;
import com.martzatech.vdhg.crmprojectback.domains.chat.enums.ChatRoomTypeEnum;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

import static com.martzatech.vdhg.crmprojectback.domains.chat.services.ChatRoomService.getByType;

public class SpecificationHelper {
    public static final int ID_STATUS_ACTIVE = 1;

    /**
     * Function Specification criteria databse
     *
     * @param statusId
     * @param specification
     * @return
     */
    public static ProductSpecification addSoftDeleteProduct(final Integer statusId, final ProductSpecification specification) {
        final ProductSpecification bySoftDelete = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.<Integer>get("status").get("id"), statusId);
        return (root, query, criteriaBuilder) ->
                Objects.nonNull(specification)
                        ? criteriaBuilder
                        .and(
                                specification.toPredicate(root, query, criteriaBuilder),
                                bySoftDelete.toPredicate(root, query, criteriaBuilder)
                        )
                        : criteriaBuilder
                        .and(
                                bySoftDelete.toPredicate(root, query, criteriaBuilder)
                        );
    }

    public static UserSpecification WhiteTypeAssociated(final String type, final UserSpecification specification) {
        final UserSpecification typeUser = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.<String>get("status").get("id"), DeletedStatusEnum.ACTIVE.getId());

        return (root, query, criteriaBuilder) ->
                Objects.nonNull(specification)
                        ? criteriaBuilder
                        .and(
                                specification.toPredicate(root, query, criteriaBuilder),
                                typeUser.toPredicate(root, query, criteriaBuilder)
                        ).in(
                                criteriaBuilder.equal(root.<Integer>get("typeUser"), "ASSOCIATE"),
                                criteriaBuilder.equal(root.<Integer>get("typeUser"), "CUSTOMER")

                        )
                        : criteriaBuilder
                        .and(
                                typeUser.toPredicate(root, query, criteriaBuilder)
                        ).in(
                                criteriaBuilder.equal(root.<Integer>get("typeUser"), "ASSOCIATE"),
                                criteriaBuilder.equal(root.<Integer>get("typeUser"), "CUSTOMER")

                        );
    }


    public static ChatRoomSpecification WhenTypeCustomer(ChatRoomTypeEnum typeCustomer,
                                                         final ChatRoomSpecification specification) {
        Specification<ChatRoomEntity> specificationObj = getByType(typeCustomer);
        return (root, query, criteriaBuilder) ->
                Objects.nonNull(specification)
                        ? criteriaBuilder
                        .and(
                                specificationObj.toPredicate(root, query, criteriaBuilder)
                        )
                        : criteriaBuilder
                        .and(
                                specificationObj.toPredicate(root, query, criteriaBuilder)
                        );
    }

    public static ChatRoomSpecification WhenTypeAccountName(final String accountName, final ChatRoomSpecification specification) {
        final ChatRoomSpecification typeUser = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.<String>get("groupAccount").get("name"), accountName);

        return (root, query, criteriaBuilder) ->
                Objects.nonNull(specification)
                        ? criteriaBuilder
                        .and(
                                specification.toPredicate(root, query, criteriaBuilder),
                                typeUser.toPredicate(root, query, criteriaBuilder)
                        )
                        : criteriaBuilder
                        .and(
                                typeUser.toPredicate(root, query, criteriaBuilder),
                                criteriaBuilder.like(root.<String>get("groupAccount").get("%name%"), accountName)
                        );
    }

    public static ChatRoomSpecification WhenTypeInternal(final Integer type, final ChatRoomSpecification specification) {
        final ChatRoomSpecification typeUser = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.<Integer>get("type").get("id"), type);

        return (root, query, criteriaBuilder) ->
                Objects.nonNull(specification)
                        ? criteriaBuilder
                        .and(
                                specification.toPredicate(root, query, criteriaBuilder),
                                typeUser.toPredicate(root, query, criteriaBuilder)
                        ).in(
                                criteriaBuilder.equal(root.<Integer>get("type").get("id"), 1)
                        )
                        : criteriaBuilder
                        .and(
                                typeUser.toPredicate(root, query, criteriaBuilder)
                        ).in(
                                criteriaBuilder.equal(root.<Integer>get("type").get("id"), 1)
                        );
    }


    public static ChatMessageSpecification advanceSpecification(final String type, final ChatMessageSpecification specification) {
        final ChatMessageSpecification typeUser = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.<String>get("value"), DeletedStatusEnum.ACTIVE.getId());

        return (root, query, criteriaBuilder) ->
                Objects.nonNull(specification)
                        ? criteriaBuilder
                        .and(
                                specification.toPredicate(root, query, criteriaBuilder),
                                typeUser.toPredicate(root, query, criteriaBuilder)
                        ).in(
                                criteriaBuilder.equal(root.<Integer>get("typeUser"), "ASSOCIATE"),
                                criteriaBuilder.equal(root.<Integer>get("typeUser"), "CUSTOMER")

                        )
                        : criteriaBuilder
                        .and(
                                typeUser.toPredicate(root, query, criteriaBuilder)
                        ).in(
                                criteriaBuilder.equal(root.<Integer>get("typeUser"), "ASSOCIATE"),
                                criteriaBuilder.equal(root.<Integer>get("typeUser"), "CUSTOMER")

                        );
    }


    public static UserSpecification addUserStatus(final UserSpecification specification) {
        final UserSpecification userSpecification = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.<Integer>get("status").get("id"), ID_STATUS_ACTIVE);
        return (root, query, criteriaBuilder) ->
                Objects.nonNull(specification)
                        ? criteriaBuilder
                        .and(
                                specification.toPredicate(root, query, criteriaBuilder),
                                userSpecification.toPredicate(root, query, criteriaBuilder)
                        )
                        : criteriaBuilder
                        .and(
                                userSpecification.toPredicate(root, query, criteriaBuilder)
                        );
    }

    public static ChatOutOfficeSpecification addSoftDeleteChatOutOffice(final Integer statusId,
                                                                        final ChatOutOfficeSpecification specification) {
        final ChatOutOfficeSpecification bySoftDelete = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.<Integer>get("status").get("id"), statusId);
        return (root, query, criteriaBuilder) ->
                Objects.nonNull(specification)
                        ? criteriaBuilder
                        .and(
                                specification.toPredicate(root, query, criteriaBuilder),
                                bySoftDelete.toPredicate(root, query, criteriaBuilder)
                        )
                        : criteriaBuilder
                        .and(
                                bySoftDelete.toPredicate(root, query, criteriaBuilder)
                        );
    }

    /**
     * @param statusId
     * @param specification
     * @return
     */
    public static CustomerSpecification addSoftDeleteCustomer(final Integer statusId, final CustomerSpecification specification) {
        final CustomerSpecification bySoftDelete = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.<Integer>get("deletedStatus").get("id"), statusId);
        return (root, query, criteriaBuilder) ->
                Objects.nonNull(specification)
                        ? criteriaBuilder
                        .and(
                                specification.toPredicate(root, query, criteriaBuilder),
                                bySoftDelete.toPredicate(root, query, criteriaBuilder)
                        )
                        : criteriaBuilder
                        .and(
                                bySoftDelete.toPredicate(root, query, criteriaBuilder)
                        );
    }

    /**
     * @param statusId
     * @param specification
     * @return
     */
    public static NoteSpecification addSoftDeleteNote(final Integer statusId, final NoteSpecification specification) {
        final NoteSpecification bySoftDelete = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.<Integer>get("status").get("id"), statusId);
        return (root, query, criteriaBuilder) ->
                Objects.nonNull(specification)
                        ? criteriaBuilder
                        .and(
                                specification.toPredicate(root, query, criteriaBuilder),
                                bySoftDelete.toPredicate(root, query, criteriaBuilder)
                        )
                        : criteriaBuilder
                        .and(
                                bySoftDelete.toPredicate(root, query, criteriaBuilder)
                        );
    }

    /**
     * @param statusId
     * @param specification
     * @return
     */
    public static OfferSpecification addSoftDeleteOffer(final Integer statusId, final OfferSpecification specification) {
        final OfferSpecification bySoftDelete = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.<Integer>get("deletedStatus").get("id"), statusId);
        return (root, query, criteriaBuilder) ->
                Objects.nonNull(specification)
                        ? criteriaBuilder
                        .and(
                                specification.toPredicate(root, query, criteriaBuilder),
                                bySoftDelete.toPredicate(root, query, criteriaBuilder)
                        )
                        : criteriaBuilder
                        .and(
                                bySoftDelete.toPredicate(root, query, criteriaBuilder)
                        );
    }

    /**
     * @param statusId
     * @param specification
     * @return
     */
    public static GroupAccountSpecification addSoftDeleteGroupAccount(final Integer statusId, final GroupAccountSpecification specification) {
        final GroupAccountSpecification bySoftDelete = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.<Integer>get("status").get("id"), statusId);
        return (root, query, criteriaBuilder) ->
                Objects.nonNull(specification)
                        ? criteriaBuilder
                        .and(
                                specification.toPredicate(root, query, criteriaBuilder),
                                bySoftDelete.toPredicate(root, query, criteriaBuilder)
                        )
                        : criteriaBuilder
                        .and(
                                bySoftDelete.toPredicate(root, query, criteriaBuilder)
                        );
    }

    /**
     * @param statusId
     * @param specification
     * @return
     */
    public static CorporateSpecification addSoftDeleteCorporate(final Integer statusId, final CorporateSpecification specification) {
        final CorporateSpecification bySoftDelete = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.<Integer>get("status").get("id"), statusId);
        return (root, query, criteriaBuilder) ->
                Objects.nonNull(specification)
                        ? criteriaBuilder
                        .and(
                                specification.toPredicate(root, query, criteriaBuilder),
                                bySoftDelete.toPredicate(root, query, criteriaBuilder)
                        )
                        : criteriaBuilder
                        .and(
                                bySoftDelete.toPredicate(root, query, criteriaBuilder)
                        );
    }

    /**
     * @param statusId
     * @param specification
     * @return
     */
    public static OrderSpecification addSoftDeleteOrder(final Integer statusId, final OrderSpecification specification) {
        final OrderSpecification bySoftDelete = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.<Integer>get("deletedStatus").get("id"), statusId);
        return (root, query, criteriaBuilder) ->
                Objects.nonNull(specification)
                        ? criteriaBuilder
                        .and(
                                specification.toPredicate(root, query, criteriaBuilder),
                                bySoftDelete.toPredicate(root, query, criteriaBuilder)
                        )
                        : criteriaBuilder
                        .and(
                                bySoftDelete.toPredicate(root, query, criteriaBuilder)
                        );
    }

    /**
     * @param statusId
     * @param specification
     * @return
     */
    public static OrderSpecification addSoftDeleteOrder(final Integer statusId, final OrderMobileSpecification specification) {
        final OrderSpecification bySoftDelete = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.<Integer>get("deletedStatus").get("id"), statusId);
        return (root, query, criteriaBuilder) ->
                Objects.nonNull(specification)
                        ? criteriaBuilder
                        .and(
                                specification.toPredicate(root, query, criteriaBuilder),
                                bySoftDelete.toPredicate(root, query, criteriaBuilder)
                        )
                        : criteriaBuilder
                        .and(
                                bySoftDelete.toPredicate(root, query, criteriaBuilder)
                        );
    }

    public static ChatRoomSpecification addChatRoom(final Integer currentUserId, final ChatRoomSpecification specification) {
        final ChatRoomSpecification bySoftDelete = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.<Integer>get("members").get("id"), currentUserId);
        return (root, query, criteriaBuilder) ->
                Objects.nonNull(specification)
                        ? criteriaBuilder
                        .or(
                                specification.toPredicate(root, query, criteriaBuilder),
                                bySoftDelete.toPredicate(root, query, criteriaBuilder)
                        )
                        : criteriaBuilder
                        .and(
                                bySoftDelete.toPredicate(root, query, criteriaBuilder)
                        );
    }

    public static ChatRoomSpecification addChatRoomType(final ChatRoomSpecification specification) {
        final ChatRoomSpecification bySoftDelete = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.<Integer>get("type"), ChatRoomTypeEnum.CUSTOMER);
        return (root, query, criteriaBuilder) ->
                Objects.nonNull(specification)
                        ? criteriaBuilder
                        .or(
                                specification.toPredicate(root, query, criteriaBuilder),
                                bySoftDelete.toPredicate(root, query, criteriaBuilder)
                        )
                        : criteriaBuilder
                        .and(
                                bySoftDelete.toPredicate(root, query, criteriaBuilder)
                        );
    }

    /**
     * @param statusId
     * @param specification
     * @return
     */
    public static LeadSpecification addSoftDeleteLead(final Integer statusId, final LeadSpecification specification) {
        final LeadSpecification bySoftDelete = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.<Integer>get("deletedStatus").get("id"), statusId);
        return (root, query, criteriaBuilder) ->
                Objects.nonNull(specification)
                        ? criteriaBuilder
                        .and(
                                specification.toPredicate(root, query, criteriaBuilder),
                                bySoftDelete.toPredicate(root, query, criteriaBuilder)
                        )
                        : criteriaBuilder
                        .and(
                                bySoftDelete.toPredicate(root, query, criteriaBuilder)
                        );
    }

}
