package com.martzatech.vdhg.crmprojectback.domains.commons.repositories;

import com.martzatech.vdhg.crmprojectback.domains.commons.entities.LanguageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepository extends JpaRepository<LanguageEntity, Integer> {

}
