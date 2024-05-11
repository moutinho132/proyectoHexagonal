package com.martzatech.vdhg.crmprojectback.domains.customers.specifications;

import com.martzatech.vdhg.crmprojectback.domains.customers.entities.CustomerEntity;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Conjunction;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Or;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

@Conjunction({
    @Or({
        @Spec(path = "person.name", params = "name", spec = Like.class),
        @Spec(path = "person.surname", params = "name", spec = Like.class),
        @Spec(path = "person.emails.value", params = "name", spec = Like.class),
        @Spec(path = "person.fullName", params = "name", spec = Like.class),
    }),
    @Or({
        @Spec(path = "person.phones.code", params = "phone", spec = Equal.class),
        @Spec(path = "person.phones.value", params = "phone", spec = Like.class)
    }),
    @Or({
        @Spec(path = "company.name", params = "company", spec = Like.class),
    }),
    @Or({
        @Spec(path = "person.nationality.id", params = "nationality", spec = Equal.class),
    }),
    @Or({
        @Spec(path = "status.id", params = "status", spec = Equal.class),
    }),
    @Or({
        @Spec(path = "creationType.id", params = "creation-type", spec = Equal.class),
    }),
    @Or({
        @Spec(path = "membership.id", params = "membership", spec = Equal.class),
    }),
    @Or({
        @Spec(path = "reference", params = "reference", spec = Like.class),
    }),
    @Or({
            @Spec(path = "deletedStatus.id", params = "deletedStatus", spec = Equal.class),
    }),


})
public interface CustomerSpecification extends Specification<CustomerEntity> {

}
