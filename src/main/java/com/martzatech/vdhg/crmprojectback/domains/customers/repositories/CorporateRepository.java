package com.martzatech.vdhg.crmprojectback.domains.customers.repositories;

import com.martzatech.vdhg.crmprojectback.domains.customers.entities.CorporateEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CorporateRepository extends JpaRepository<CorporateEntity, Integer>,
    JpaSpecificationExecutor<CorporateEntity> {

    @Modifying
    @Transactional
    @Query(value = "update CorporateEntity c set c.status.id=2 where c.id = :id")
    void deleteStatus(@Param("id") Integer id);

    @Override
    @Query(value = "select c  from CorporateEntity c where c.id = :id and c.status.id=1")
    Optional<CorporateEntity> findById(Integer id);
}
