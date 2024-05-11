package com.martzatech.vdhg.crmprojectback.domains.chat.repositories;

import com.martzatech.vdhg.crmprojectback.domains.chat.entities.ChatOutOfficeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatOutOfficeRepository extends JpaRepository<ChatOutOfficeEntity, Integer>,
    JpaSpecificationExecutor<ChatOutOfficeEntity> {
}
