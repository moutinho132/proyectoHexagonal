package com.martzatech.vdhg.crmprojectback.domains.customers.specifications;

import com.martzatech.vdhg.crmprojectback.domains.wallet.entities.WalletEntity;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Conjunction;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Or;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

@Conjunction({
        @Or({
                @Spec(path = "id", params = "id", spec = Equal.class),
        })
})
public interface WalletSpecification extends Specification<WalletEntity> {

}
