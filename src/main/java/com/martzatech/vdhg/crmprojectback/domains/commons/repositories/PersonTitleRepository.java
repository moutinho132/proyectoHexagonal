package com.martzatech.vdhg.crmprojectback.domains.commons.repositories;

import com.martzatech.vdhg.crmprojectback.domains.commons.entities.PersonTitleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonTitleRepository extends JpaRepository<PersonTitleEntity, Integer> {

  boolean existsByNameIgnoreCase(String name);
}
