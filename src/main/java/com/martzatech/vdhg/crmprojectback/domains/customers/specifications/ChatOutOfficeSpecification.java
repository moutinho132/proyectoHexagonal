package com.martzatech.vdhg.crmprojectback.domains.customers.specifications;

import com.martzatech.vdhg.crmprojectback.domains.chat.entities.ChatOutOfficeEntity;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.LessThanOrEqual;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Conjunction;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Or;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

@Conjunction({
    @Or({
        @Spec(path = "value", params = "value", spec = Like.class),
        @Spec(path = "id", params = "id", spec = Equal.class),
    }),
    @Or({
        @Spec(path = "start", params = "start", spec = LessThanOrEqual.class),
    }),
    @Or({
        @Spec(path = "end", params = "end", spec = LessThanOrEqual.class),
    }),
    @Or({
            @Spec(path = "days.id", params = "days", spec = Equal.class),
    })
})
public interface ChatOutOfficeSpecification extends Specification<ChatOutOfficeEntity> {

}
