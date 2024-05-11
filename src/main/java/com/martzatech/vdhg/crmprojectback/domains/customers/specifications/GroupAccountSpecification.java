package com.martzatech.vdhg.crmprojectback.domains.customers.specifications;

import com.martzatech.vdhg.crmprojectback.domains.customers.entities.GroupAccountEntity;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Conjunction;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Or;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

@Conjunction({
        @Or({
                @Spec(path = "owner.person.name", params = "owner", spec = Like.class),
                @Spec(path = "owner.person.surname", params = "owner", spec = Like.class),
                @Spec(path = "owner.person.emails.value", params = "owner", spec = Like.class),
        }),
        @Or({
                @Spec(path = "associates.name", params = "associates", spec = Like.class),
                @Spec(path = "associates.surname", params = "associates", spec = Like.class),
                @Spec(path = "associates.email", params = "associates", spec = Like.class),
        }),
        @Or({
                @Spec(path = "name", params = "name", spec = Like.class),
        }),
        @Or({
                @Spec(path = "owner.membership.id", params = "membership", spec = Equal.class),
        }),
        @Or({
                @Spec(path = "email", params = "email", spec = Like.class),
        }),
        @Or({
                @Spec(path = "alias", params = "alias", spec = Like.class),
        }),
        @Or({
                @Spec(path = "industry", params = "industry", spec = Like.class),
        }),
        @Or({
                @Spec(path = "vat", params = "vat", spec = Like.class),
        }),
        @Or({
                @Spec(path = "status.id", params = "status", spec = Like.class),
        }),
})
public interface GroupAccountSpecification extends Specification<GroupAccountEntity> {

}
