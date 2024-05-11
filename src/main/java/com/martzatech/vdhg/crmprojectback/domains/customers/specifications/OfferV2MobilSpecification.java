package com.martzatech.vdhg.crmprojectback.domains.customers.specifications;

import com.martzatech.vdhg.crmprojectback.domains.customers.entities.CustomerEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.GroupAccountEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.OfferEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.enums.OfferGLobalStatusEnum;
import com.martzatech.vdhg.crmprojectback.domains.customers.enums.OfferStatusEnum;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Customer;
import com.martzatech.vdhg.crmprojectback.domains.security.entities.UserEntity;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Getter
@Setter
public class OfferV2MobilSpecification implements Specification<OfferEntity> {
    private OfferGLobalStatusEnum globalStatus;
    private Customer customers;
    private OfferStatusEnum status;
    private Boolean active;
    private String name;
    private BigDecimal priceFrom;
    private BigDecimal priceTo;
    private Boolean restricted;


    @Override
    public Predicate toPredicate(Root<OfferEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();
        List<Order> orders = new ArrayList<>();
        query.distinct(true);

        Join<CustomerEntity, UserEntity> userJoin = root.join("customer", JoinType.INNER);

        Join<CustomerEntity, GroupAccountEntity> groupAccountJoin = userJoin.join("groupAccount", JoinType.LEFT);

        if (Objects.nonNull(customers)) {
            predicates.add(cb.equal(root.get("customer").get("id"), customers.getId()));
        }else{
            predicates.add(cb.equal(root.get("restricted").as(Boolean.class), Boolean.FALSE));
        }

        if(active==Boolean.TRUE){
            predicates.add(cb.equal(root.get("active"), active));
        }

        if (StringUtils.isNotBlank(name)) {
            predicates.add(cb.like(root.get("name"), "%" + name + "%"));
        }

        if(Objects.nonNull(priceFrom) && priceTo!= BigDecimal.ZERO){
            predicates.add(cb.greaterThanOrEqualTo(root.get("total"), priceFrom));

        }

        if(Objects.nonNull(priceTo) && priceTo!= BigDecimal.ZERO){
            predicates.add(cb.lessThanOrEqualTo(root.get("total"), priceTo));
        }

        predicates.add(cb.equal(root.get("globalStatus"), OfferGLobalStatusEnum.WORKING));
        predicates.add(cb.equal(root.get("status"), OfferStatusEnum.SENT));



       /* // Condición restricted
        if (Objects.equals(root.get("restricted").as(Boolean.class), Boolean.TRUE)) {
            predicates.add(cb.isNull(userJoin.get("associated").get("id")));
        }*/

        return cb.and(predicates.toArray(new Predicate[predicates.size()]));
    }
}

