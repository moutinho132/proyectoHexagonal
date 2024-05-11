package com.martzatech.vdhg.crmprojectback.domains.security.repositories;

import com.martzatech.vdhg.crmprojectback.domains.security.entities.SubsidiaryEntity;
import com.martzatech.vdhg.crmprojectback.domains.security.models.Subsidiary;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SubsidiaryRepository extends JpaRepository<SubsidiaryEntity, Integer> {
  @Query(value = "select   s from SubsidiaryEntity s where   s.id not in(6)") //TODO:consultar
  @Override
  List<SubsidiaryEntity> findAll(Sort sort);
  Optional<Subsidiary> findByName(String name);

  boolean existsByNameIgnoreCase(String name);
}
