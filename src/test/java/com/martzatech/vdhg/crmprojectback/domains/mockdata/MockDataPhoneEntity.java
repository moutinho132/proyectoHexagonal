package com.martzatech.vdhg.crmprojectback.domains.mockdata;

import com.martzatech.vdhg.crmprojectback.application.enums.DeletedStatusEnum;
import com.martzatech.vdhg.crmprojectback.domains.commons.entities.PhoneEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.DeletedEntity;

import java.time.LocalDateTime;

public class MockDataPhoneEntity {

    public static PhoneEntity builderPhoneEntity (){
        return PhoneEntity
                .builder()
                .id(433)
                .deletedStatus(DeletedEntity.builder().id(DeletedStatusEnum.ACTIVE.getId()).build())
                .value("123131231231231")
                .code("+355")
                .creationTime(LocalDateTime.now())
                .build();
    }
}
