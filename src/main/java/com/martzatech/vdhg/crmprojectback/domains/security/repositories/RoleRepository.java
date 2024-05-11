package com.martzatech.vdhg.crmprojectback.domains.security.repositories;

import com.martzatech.vdhg.crmprojectback.domains.security.entities.RoleEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Integer>,
        JpaSpecificationExecutor<RoleEntity> {

  boolean existsByNameIgnoreCase(String name);
  @Query(value = "select  r from RoleEntity r where r.status.id = 1 and r.department.id not in(2)")
  @Override
  List<RoleEntity> findAll(Sort sort);

  @Modifying
  @Transactional
  @Query(value = "update RoleEntity r set r.status.id=2 where r.id = :id")
  void deleteRoleByStatus(@Param("id") Integer id);
}
