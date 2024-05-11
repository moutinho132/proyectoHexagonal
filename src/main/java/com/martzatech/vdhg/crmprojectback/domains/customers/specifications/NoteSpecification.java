package com.martzatech.vdhg.crmprojectback.domains.customers.specifications;

import com.martzatech.vdhg.crmprojectback.domains.customers.entities.NoteEntity;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.GreaterThanOrEqual;
import net.kaczmarzyk.spring.data.jpa.domain.LessThanOrEqual;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Conjunction;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Or;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

@Conjunction({
    @Or({
        @Spec(path = "title", params = "text", spec = Like.class),
        @Spec(path = "description", params = "text", spec = Like.class),
    }),
    @Or({
        @Spec(path = "typeAsInt", params = "type", spec = Equal.class),
    }),
    @Or({
        @Spec(path = "elementId", params = "element", spec = Equal.class),
    }),
    @Or({
        @Spec(path = "creationUser.id", params = "owner", spec = Equal.class),
    }),
    @Or({
        @Spec(path = "users.id", params = "user", spec = Equal.class),
    }),
    @Or({
        @Spec(path = "roles.id", params = "role", spec = Equal.class),
    }),
    @Or({
        @Spec(path = "creationTimeAsStr", params = "from", spec = GreaterThanOrEqual.class),
    }),
    @Or({
        @Spec(path = "creationTimeAsStr", params = "to", spec = LessThanOrEqual.class),
    }),
    @Or({
        @Spec(path = "status.id", params = "status", spec = Equal.class),
    })
})
public interface NoteSpecification extends Specification<NoteEntity> {

}
