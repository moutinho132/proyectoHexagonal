package com.martzatech.vdhg.crmprojectback.domains.vendors.specifications;

import com.martzatech.vdhg.crmprojectback.domains.vendors.entities.VendorEntity;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Conjunction;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Or;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

@Conjunction({
        @Or({
                @Spec(path = "id", params = "id", spec = Equal.class),
                @Spec(path = "name", params = "name", spec = Like.class),
                @Spec(path = "vat", params = "vat", spec = Like.class),
                @Spec(path = "financeEmail", params = "financeEmail", spec = Like.class),
        }),
        @Or({
                @Spec(path = "vendorLocations.name", params = "nameLocation", spec = Like.class),
        }),
})
public interface VendorSpecification extends Specification<VendorEntity> {
}
