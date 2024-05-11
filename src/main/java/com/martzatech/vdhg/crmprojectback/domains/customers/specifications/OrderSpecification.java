package com.martzatech.vdhg.crmprojectback.domains.customers.specifications;

import com.martzatech.vdhg.crmprojectback.domains.customers.entities.OrderEntity;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.GreaterThanOrEqual;
import net.kaczmarzyk.spring.data.jpa.domain.LessThanOrEqual;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Conjunction;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Or;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

@Conjunction(
    {@Or({
            @Spec(path = "id", params = "id", spec = Equal.class),
    }),
    @Or({
        @Spec(path = "offer.name", params = "name", spec = Like.class),
        @Spec(path = "offer.description", params = "name", spec = Like.class)
    }),
    @Or({
        @Spec(path = "offer.number", params = "number", spec = Equal.class),
    }),
    @Or({
        @Spec(path = "offer.currency", params = "currency", spec = Equal.class),
    }),
    @Or({
        @Spec(path = "offer.customer.id", params = "customer", spec = Equal.class),
    }),
    @Or({
        @Spec(path = "offer.products.product.id", params = "product", spec = Equal.class),
    }),
    @Or({
        @Spec(path = "offer.total", params = "priceFrom", spec = GreaterThanOrEqual.class),
    }),
    @Or({
        @Spec(path = "offer.total", params = "priceTo", spec = LessThanOrEqual.class),
    }),
    @Or({
        @Spec(path = "status", params = "status", spec = Equal.class),
    }),
})
public interface OrderSpecification extends Specification<OrderEntity> {

}
