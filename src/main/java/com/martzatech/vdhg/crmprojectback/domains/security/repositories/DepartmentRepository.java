package com.martzatech.vdhg.crmprojectback.domains.security.repositories;

import com.martzatech.vdhg.crmprojectback.domains.security.entities.DepartmentEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Integer> {

  @Query(value = "select  d from DepartmentEntity d where d.status.id = 1 and d.subsidiary.id not in(6)")
  @Override
  List<DepartmentEntity> findAll(Sort sort);

  boolean existsByNameIgnoreCase(String name);

  @Modifying
  @Transactional
  @Query(value = "update DepartmentEntity d set d.status.id=2 where d.id = :id")
  void deleteDepartmentByStatus(@Param("id") Integer id);
}
