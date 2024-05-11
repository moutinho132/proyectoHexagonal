package com.martzatech.vdhg.crmprojectback.domains.commons.repositories;

import com.martzatech.vdhg.crmprojectback.domains.commons.entities.IdentityDocumentTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdentityDocumentTypeRepository extends JpaRepository<IdentityDocumentTypeEntity, Integer> {

  boolean existsByNameIgnoreCase(String name);
}
