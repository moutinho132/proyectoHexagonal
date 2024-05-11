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
public class ChatRoomv2Specification implements Specification<ChatRoomEntity> {
    private  final  Boolean ARCHIVE = Boolean.TRUE;
    @Override
    public Predicate toPredicate(Root<ChatRoomEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();
        List<Order> orders = new ArrayList<>();

            predicates.add(cb.equal(root.get("archive"), ARCHIVE));


        return cb.and(predicates.toArray(new Predicate[predicates.size()]));
    }
}
