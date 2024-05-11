package com.martzatech.vdhg.crmprojectback.domains.customers.specifications;

import com.martzatech.vdhg.crmprojectback.domains.security.entities.RoleEntity;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Conjunction;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Or;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

@Conjunction({
        @Or({
                @Spec(path = "name", params = "name", spec = Like.class),
                @Spec(path = "description", params = "name", spec = Like.class),
        }),
        @Or({
                @Spec(path = "department.id", params = "department", spec = Equal.class),
        }),
        @Or({
                @Spec(path = "status.id", params = "status", spec = Equal.class),
        })
})
public interface RolSpecification extends Specification<RoleEntity> {

}
