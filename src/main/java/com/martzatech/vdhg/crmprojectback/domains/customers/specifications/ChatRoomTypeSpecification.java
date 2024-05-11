package com.martzatech.vdhg.crmprojectback.domains.customers.specifications;

import com.martzatech.vdhg.crmprojectback.domains.chat.entities.ChatRoomEntity;
import com.martzatech.vdhg.crmprojectback.domains.chat.enums.ChatRoomTypeEnum;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Getter
public class ChatRoomTypeSpecification implements Specification<ChatRoomEntity> {
    private String type;
    private Boolean priority;
    @Override
    public Predicate toPredicate(Root<ChatRoomEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();
        List<Order> orders = new ArrayList<>();
        if (StringUtils.isNotBlank(type)) {
            if (type.toUpperCase().equals(ChatRoomTypeEnum.CUSTOMER.name())) {
                predicates.add(
                        cb.or(cb.equal(root.get("type"), ChatRoomTypeEnum.CUSTOMER)
                                //cb.equal(root.get("type"), ChatRoomTypeEnum.GROUPACCOUNT)//TODO:CONVERSARLO
                        )
                );
                orders.add(cb.desc(root.get("type")));
            }

            if (type.toUpperCase().equals(ChatRoomTypeEnum.INTERNAL.name())) {
                predicates.add(cb.equal(root.get("type"), ChatRoomTypeEnum.INTERNAL));
            }
        }

        if (Objects.nonNull(priority) && priority) {
            predicates.add(cb.equal(root.get("members").get("customer").get("membership").get("priority"), priority));
            orders.add(cb.desc(root.get("members").get("customer").get("membership").get("priority")));
        }else if (Objects.nonNull(priority) && !priority) {
            predicates.add(cb.equal(root.get("members").get("customer").get("membership").get("priority"), priority));
            orders.add(cb.desc(root.get("members").get("customer").get("membership").get("priority")));
        }
        orders.add(cb.desc(root.get("creationTime")));

        query.orderBy(orders);
        // how to make it  dynamic??Since root.get will not accept joined column...Is there any way to sort based on User requirement may be single sort may be multiple sort...may be from different table?
        return cb.and(predicates.toArray(new Predicate[predicates.size()]));
    }
}
