package com.martzatech.vdhg.crmprojectback.domains.customers.specifications;

import com.martzatech.vdhg.crmprojectback.domains.customers.entities.CorporateEntity;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Conjunction;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Or;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

@Conjunction({
    @Or({
        @Spec(path = "mainContact.person.name", params = "mainContact", spec = Like.class),
        @Spec(path = "mainContact.person.surname", params = "mainContact", spec = Like.class),
        @Spec(path = "mainContact.person.emails.value", params = "mainContact", spec = Like.class),
    }),
    @Or({
        @Spec(path = "name", params = "name", spec = Like.class),
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
})
public interface CorporateSpecification extends Specification<CorporateEntity> {

}
