package com.martzatech.vdhg.crmprojectback.domains.security.repositories;

import com.martzatech.vdhg.crmprojectback.domains.security.entities.UserEntity;
import com.martzatech.vdhg.crmprojectback.domains.security.entities.UserMobilEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMobilRepository extends JpaRepository<UserMobilEntity, Integer>, JpaSpecificationExecutor<UserMobilEntity> {
    UserMobilEntity findByUser(final UserEntity user);
}
