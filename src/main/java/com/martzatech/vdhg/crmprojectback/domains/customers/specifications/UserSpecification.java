package com.martzatech.vdhg.crmprojectback.domains.customers.specifications;

import com.martzatech.vdhg.crmprojectback.domains.security.entities.UserEntity;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Conjunction;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Or;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

@Conjunction({
        @Or({
                @Spec(path = "id", params = "id", spec = Equal.class),
                @Spec(path = "title", params = "title", spec = Like.class),
                @Spec(path = "name", params = "name", spec = Like.class),
                @Spec(path = "surname", params = "name", spec = Like.class),
                @Spec(path = "email", params = "name", spec = Like.class),
                @Spec(path = "typeUser", params = "typeUser", spec = Equal.class),
                //@Spec(path = "fullName", params = "name", spec = Like.class),

        }),
        @Or({
                @Spec(path = "status.id", params = "status", spec = Equal.class),
        }),
        @Or({
                @Spec(path = "customer.person.name", params = "customer", spec = Like.class),
                @Spec(path = "customer.person.surname", params = "customer", spec = Like.class),
                // @Spec(path = "customer.emails.value", params = "customerName", spec = Like.class),
        }),
        @Or({
                @Spec(path = "associated.name", params = "associated", spec = Like.class),
                @Spec(path = "associated.surname", params = "associated", spec = Like.class),
                @Spec(path = "associated.email", params = "associated", spec = Like.class),
        }),

})
public interface UserSpecification extends Specification<UserEntity> {

}
