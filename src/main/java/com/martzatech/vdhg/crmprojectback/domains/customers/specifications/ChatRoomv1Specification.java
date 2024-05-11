package com.martzatech.vdhg.crmprojectback.domains.customers.specifications;

import com.martzatech.vdhg.crmprojectback.domains.chat.entities.ChatRoomEntity;
import com.martzatech.vdhg.crmprojectback.domains.chat.enums.ChatRoomTypeEnum;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Getter
@Setter
public class ChatRoomv1Specification implements Specification<ChatRoomEntity> {
    private String type;
    private String accountName;
    private Integer membership;
    private Integer members;
    private String membersName;
    private Integer id;
    private String name;
    private Boolean unRead;
    private Boolean priority;

    @Override
    public Predicate toPredicate(Root<ChatRoomEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();
        List<Order> orders = new ArrayList<>();
        query.distinct(true);

        if (StringUtils.isNotBlank(type)) {
            if (type.toUpperCase().equals(ChatRoomTypeEnum.CUSTOMER.name())) {
                predicates.add(
                        cb.or(cb.equal(root.get("type"), ChatRoomTypeEnum.CUSTOMER),
                                cb.equal(root.get("type"), ChatRoomTypeEnum.GROUPACCOUNT)
                        )
                );
            }
            if (type.toUpperCase().equals(ChatRoomTypeEnum.INTERNAL.name())) {
                predicates.add(cb.equal(root.get("type"), ChatRoomTypeEnum.INTERNAL));
            }
        }

        if (StringUtils.isNotBlank(accountName)) {
            predicates.add(cb.like(root.get("groupAccount").get("name"), "%" + accountName + "%"));
        }

        if (Objects.nonNull(membership)) {
            predicates.add(cb.equal(root.get("members").get("customer").get("membership").get("id"), membership));
        }

        if (Objects.nonNull(members)) {
            predicates.add(cb.equal(root.get("members").get("id"), members));
        }
        if (Objects.nonNull(id)) {
            predicates.add(cb.equal(root.get("id"), id));
        }
        if (StringUtils.isNotBlank(name)) {
            predicates.add(cb.like(root.get("name"), name));
        }

        if (StringUtils.isNotBlank(membersName)) {
            //predicates.add(cb.like(root.get("members").get("name"), "%"+membersName+"%"));
            //predicates.add(cb.like(root.get("members").get("surname"),  "%"+membersName+"%"));
            predicates.add(cb.like(root.get("members").get("fullName"), "%" + membersName + "%"));
            //orders.add(cb.desc(root.get("members").get("name")));
        }


        if (Objects.nonNull(priority) && priority) {
            predicates.add(cb.equal(root.get("members").get("customer").get("membership").get("priority"), priority));

            orders.add(cb.desc(root.get("members").get("customer").get("membership").get("priority")));
        } else if (Objects.nonNull(priority) && !priority) {
            predicates.add(cb.equal(root.get("members").get("customer").get("membership").get("priority"), priority));
            orders.add(cb.desc(root.get("members").get("customer").get("membership").get("priority")));

        }

        return cb.and(predicates.toArray(new Predicate[predicates.size()]));
    }
}
