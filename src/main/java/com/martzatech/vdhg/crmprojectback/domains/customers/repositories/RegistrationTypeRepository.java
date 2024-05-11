package com.martzatech.vdhg.crmprojectback.domains.customers.repositories;

import com.martzatech.vdhg.crmprojectback.domains.customers.entities.RegistrationTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationTypeRepository extends JpaRepository<RegistrationTypeEntity, Integer> {

  boolean existsByNameIgnoreCase(String name);
}
