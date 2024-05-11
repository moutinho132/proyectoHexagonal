package com.martzatech.vdhg.crmprojectback.domains.commons.repositories;

import com.martzatech.vdhg.crmprojectback.domains.commons.entities.CivilStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CivilStatusRepository extends JpaRepository<CivilStatusEntity, Integer> {

  boolean existsByNameIgnoreCase(String name);
}
