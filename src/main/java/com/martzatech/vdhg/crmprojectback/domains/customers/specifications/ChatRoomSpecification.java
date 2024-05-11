package com.martzatech.vdhg.crmprojectback.domains.customers.specifications;

import com.martzatech.vdhg.crmprojectback.domains.chat.entities.ChatRoomEntity;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.LessThanOrEqual;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Conjunction;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Or;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

@Conjunction({
        @Or({
                @Spec(path = "id", params = "id", spec = Equal.class),
                @Spec(path = "name", params = "name", spec = Like.class),
                @Spec(path = "creationTime", params = "creationTime", spec = LessThanOrEqual.class),
        }),
        @Or({
                @Spec(path = "type", params = "type", spec = Equal.class),
        }),
        @Or({
                @Spec(path = "members.id", params = "members", spec = Equal.class),
                @Spec(path = "members.name", params = "membersName", spec = Like.class),
                @Spec(path = "members.surname", params = "membersName", spec = Like.class),
                @Spec(path = "members.email", params = "membersName", spec = Like.class),
                @Spec(path = "members.customer.membership.id", params = "membership", spec = Equal.class)
        }),
        @Or({
                @Spec(path = "groupAccount.name", params = "accountName", spec = Like.class),
        }),
        @Or({
                @Spec(path = "members.typeUser", params = "typeUser", spec = Like.class),
        }),
})
public interface ChatRoomSpecification extends Specification<ChatRoomEntity> {

}
