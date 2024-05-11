package com.martzatech.vdhg.crmprojectback.domains.customers.repositories;

import com.martzatech.vdhg.crmprojectback.domains.customers.entities.NoteEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<NoteEntity, Integer>,
    JpaSpecificationExecutor<NoteEntity> {

    @Override
    @Query(value = "select n from NoteEntity n where n.id = :id and n.status.id=1")
    Optional<NoteEntity> findById(Integer id);

    List<NoteEntity> findByElementId(Integer elementId);
    @Modifying
    @Query(value = "update NoteEntity n set n.status.id=2 where n.id = :id")
    @Transactional
    void deleteStatus(@Param("id") Integer id);
}
