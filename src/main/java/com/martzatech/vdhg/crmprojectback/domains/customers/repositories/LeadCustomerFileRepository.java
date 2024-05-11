package com.martzatech.vdhg.crmprojectback.domains.customers.repositories;

import com.martzatech.vdhg.crmprojectback.domains.customers.entities.LeadCustomerFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LeadCustomerFileRepository extends JpaRepository<LeadCustomerFileEntity, Integer> {
    @Modifying
    @Query(value = "update LeadCustomerFileEntity p set p.status.id=2 where p.id = :id")
    @Override
    void deleteById(@Param("id") Integer id);
}
