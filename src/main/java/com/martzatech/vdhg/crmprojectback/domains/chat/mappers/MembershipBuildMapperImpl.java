package com.martzatech.vdhg.crmprojectback.domains.chat.mappers;

import com.martzatech.vdhg.crmprojectback.domains.customers.models.Membership;
import com.martzatech.vdhg.crmprojectback.domains.security.entities.UserEntity;

import java.util.Objects;

public class MembershipBuildMapperImpl {
    public static Membership buildMembers(UserEntity entity) {
        Membership membership = null;
        if(Objects.nonNull(entity.getCustomer())){
            membership =   Membership.builder()
                    .id(entity.getCustomer().getMembership().getId())
                    .name(entity.getCustomer().getMembership().getName())
                    .priority(entity.getCustomer().getMembership().getPriority())
                    .build();
        }
        return membership;
    }
}
