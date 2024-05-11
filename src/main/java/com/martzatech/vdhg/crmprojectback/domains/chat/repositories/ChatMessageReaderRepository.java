package com.martzatech.vdhg.crmprojectback.domains.chat.repositories;

import com.martzatech.vdhg.crmprojectback.domains.chat.entities.ChatMessageReaderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatMessageReaderRepository extends JpaRepository<ChatMessageReaderEntity, Integer> {

    @Query("select e from ChatMessageReaderEntity e where e.id = :id")
    Optional<ChatMessageReaderEntity> findById(@Param("id") Integer id);
}
