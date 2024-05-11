package com.martzatech.vdhg.crmprojectback.domains.security.repositories;

import com.martzatech.vdhg.crmprojectback.domains.security.entities.AccessGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessGroupRepository extends JpaRepository<AccessGroupEntity, Integer> {

}
