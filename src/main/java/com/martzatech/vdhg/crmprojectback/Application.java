package com.martzatech.vdhg.crmprojectback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(
        basePackages = {
                "com.martzatech.vdhg.crmprojectback.domains.commons.entities",
                "com.martzatech.vdhg.crmprojectback.domains.customers.entities",
                "com.martzatech.vdhg.crmprojectback.domains.vendors.entities",
                "com.martzatech.vdhg.crmprojectback.domains.security.entities",
                "com.martzatech.vdhg.crmprojectback.domains.chat.entities",
                "com.martzatech.vdhg.crmprojectback.domains.vendors.entities",
                "com.martzatech.vdhg.crmprojectback.domains.wallet.entities"
        })
@EnableJpaRepositories(basePackages = {
        "com.martzatech.vdhg.crmprojectback.domains.commons.repositories",
        "com.martzatech.vdhg.crmprojectback.domains.customers.repositories",
        "com.martzatech.vdhg.crmprojectback.domains.security.repositories",
        "com.martzatech.vdhg.crmprojectback.domains.chat.repositories",
        "com.martzatech.vdhg.crmprojectback.domains.vendors.repositories",
        "com.martzatech.vdhg.crmprojectback.domains.wallet.repositories"
})
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
