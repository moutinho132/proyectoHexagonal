package com.martzatech.vdhg.crmprojectback.domains.customers.specifications;

import com.martzatech.vdhg.crmprojectback.domains.customers.entities.PreOfferEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.enums.OfferStatusEnum;
import com.martzatech.vdhg.crmprojectback.domains.customers.enums.PreOfferStatusEnum;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Customer;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Getter
@Setter
public class PreOfferV2MobilSpecification implements Specification<PreOfferEntity> {
    private PreOfferStatusEnum globalStatus; // TODO: pendiente
    private Customer customers;
    private OfferStatusEnum status;

    @Override
    public Predicate toPredicate(Root<PreOfferEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();
        List<Order> orders = new ArrayList<>();
        query.distinct(true);

        if (Objects.nonNull(customers)) {
            predicates.add(cb.equal(root.get("customers").get("id"), customers.getId()));
        }/*else{
            predicates.add(cb.equal(root.get("restricted").as(Boolean.class), Boolean.FALSE));
        }*/
        predicates.add(cb.equal(root.get("globalStatus"), PreOfferStatusEnum.WORKING));
        predicates.add(cb.equal(root.get("status"), OfferStatusEnum.SENT));
        return cb.and(predicates.toArray(new Predicate[predicates.size()]));
    }
}
