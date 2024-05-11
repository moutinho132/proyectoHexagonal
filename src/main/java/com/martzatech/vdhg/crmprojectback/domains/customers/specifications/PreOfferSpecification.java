package com.martzatech.vdhg.crmprojectback.domains.customers.specifications;

import com.martzatech.vdhg.crmprojectback.domains.customers.entities.PreOfferEntity;
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
        @Spec(path = "name", params = "name", spec = Like.class),
        @Spec(path = "description", params = "name", spec = Like.class)
    }),
    @Or({
        @Spec(path = "number", params = "number", spec = Equal.class),
    }),
    @Or({
        @Spec(path = "version", params = "version", spec = Equal.class),
    }),
    @Or({
        @Spec(path = "currency", params = "currency", spec = Equal.class),
    }),
    @Or({
        @Spec(path = "products.product.id", params = "product", spec = Equal.class),
    }),
    @Or({
        @Spec(path = "active", params = "active", spec = Equal.class),
    }),
    @Or({
        @Spec(path = "total", params = "priceFrom", spec = GreaterThanOrEqual.class),
    }),
    @Or({
        @Spec(path = "total", params = "priceTo", spec = LessThanOrEqual.class),
    }),
    @Or({
        @Spec(path = "status", params = "status", spec = Equal.class),
    }),
})
public interface PreOfferSpecification extends Specification<PreOfferEntity> {

}
