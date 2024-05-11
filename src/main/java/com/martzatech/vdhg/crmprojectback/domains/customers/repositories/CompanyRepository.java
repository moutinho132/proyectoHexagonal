package com.martzatech.vdhg.crmprojectback.domains.customers.repositories;

import com.martzatech.vdhg.crmprojectback.domains.customers.entities.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, Integer> {

    Optional<CompanyEntity> findByName(String name);

    @Override
    @Query(value = "select c  from CompanyEntity c where c.id = :id ")
    Optional<CompanyEntity> findById(Integer id);
}
