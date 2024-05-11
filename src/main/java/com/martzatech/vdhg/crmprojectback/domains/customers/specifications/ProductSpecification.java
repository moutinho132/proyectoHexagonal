package com.martzatech.vdhg.crmprojectback.domains.customers.specifications;

import com.martzatech.vdhg.crmprojectback.domains.customers.entities.ProductEntity;
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
        @Spec(path = "description", params = "name", spec = Like.class),
        @Spec(path = "marketing", params = "name", spec = Like.class),
    }),
    @Or({
        @Spec(path = "subsidiary.id", params = "subsidiary", spec = Equal.class),
    }),
    @Or({
        @Spec(path = "vendor.id", params = "vendor", spec = Equal.class),
    }),
    /*@Or({
            @Spec(path = "basePrice", params = "minBasePrice", spec = GreaterThanOrEqual.class),
            @Spec(path = "basePrice", params = "maxBasePrice", spec = LessThanOrEqual.class),
    }),*/
    @Or({
            @Spec(path = "basePrice", params = "minBasePrice", spec = GreaterThanOrEqual.class),
    }),
    @Or({
            @Spec(path = "basePrice", params = "maxBasePrice", spec = LessThanOrEqual.class),
    }),
    @Or({
        @Spec(path = "membership.id", params = "membership", spec = Equal.class),
    }),
    @Or({
        @Spec(path = "category.id", params = "category", spec = Equal.class),
    }),
    @Or({
        @Spec(path = "subCategories.id", params = "subcategory", spec = Equal.class),
    }),
    @Or({
        @Spec(path = "visibility", params = "visibility", spec = Equal.class),
    }),
    @Or({
        @Spec(path = "availabilityFromAsStr", params = "availability-from", spec = LessThanOrEqual.class),
    }),
    @Or({
        @Spec(path = "availabilityToAsStr", params = "availability-to", spec = LessThanOrEqual.class),
    }),
    @Or({
        @Spec(path = "active", params = "active", spec = Equal.class),
    })
})
public interface ProductSpecification extends Specification<ProductEntity> {

}
