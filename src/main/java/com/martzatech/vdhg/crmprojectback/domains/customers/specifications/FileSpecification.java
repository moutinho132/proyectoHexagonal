package com.martzatech.vdhg.crmprojectback.domains.customers.specifications;

import com.martzatech.vdhg.crmprojectback.domains.chat.entities.FileEntity;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Conjunction;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Or;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

@Conjunction({
        @Or({
                @Spec(path = "id", params = "id", spec = Equal.class),
                @Spec(path = "url", params = "url", spec = Like.class),
                @Spec(path = "extension", params = "extension", spec = Like.class),
                @Spec(path = "name", params = "name", spec = Like.class),
        }),

    @Or({
        @Spec(path = "status.id", params = "status", spec = Equal.class),
    })
})
public interface FileSpecification extends Specification<FileEntity> {

}
