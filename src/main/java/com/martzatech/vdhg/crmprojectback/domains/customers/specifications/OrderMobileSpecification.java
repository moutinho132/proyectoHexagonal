package com.martzatech.vdhg.crmprojectback.domains.customers.specifications;


import com.martzatech.vdhg.crmprojectback.domains.customers.entities.OrderEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.enums.OfferStatusEnum;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Customer;
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
public class OrderMobileSpecification implements Specification<OrderEntity> {
    private Customer customers;
    private Integer number;
    private BigDecimal priceFrom;
    private BigDecimal priceTo;
    private String status;

    @Override
    public Predicate toPredicate(Root<OrderEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();
        List<Order> orders = new ArrayList<>();
        query.distinct(true);

        if (Objects.nonNull(customers)) {
            predicates.add(cb.equal(root.get("offer").get("customer").get("id"), customers.getId()));
        }else{
            predicates.add(cb.equal(root.get("offer").get("restricted").as(Boolean.class), Boolean.FALSE));
        }

        if(Objects.nonNull(priceFrom) && priceTo!= BigDecimal.ZERO){
            predicates.add(cb.greaterThanOrEqualTo(root.get("offer").get("total"), priceFrom));

        }

        if(Objects.nonNull(priceTo) && priceTo!= BigDecimal.ZERO){
            predicates.add(cb.lessThanOrEqualTo(root.get("offer").get("total"), priceTo));
        }

        if(StringUtils.isNotBlank(status)){
            if(status.equals(OfferStatusEnum.ACCEPTED)){
                predicates.add(cb.equal(root.get("offer").get("status"), OfferStatusEnum.ACCEPTED));
            }else{
                predicates.add(cb.equal(root.get("offer").get("status"), OfferStatusEnum.CONFIRMED));
            }
        }
        return cb.and(predicates.toArray(new Predicate[predicates.size()]));
    }

}
