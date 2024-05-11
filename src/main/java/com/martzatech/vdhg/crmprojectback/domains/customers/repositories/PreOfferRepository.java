package com.martzatech.vdhg.crmprojectback.domains.customers.repositories;

import com.martzatech.vdhg.crmprojectback.domains.customers.entities.PreOfferEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PreOfferRepository extends JpaRepository<PreOfferEntity, Integer>,
    JpaSpecificationExecutor<PreOfferEntity> {

  @Query(value = "select max(e.number) from PreOfferEntity e")
  Integer getMaxNumber();

  @Query(value = "select max(e.version) from PreOfferEntity e where e.number=:number")
  Integer getMaxVersion(@Param("number") Integer number);

  boolean existsByNumber(Integer number);

  List<PreOfferEntity> findByNumber(Integer number);

  Optional<PreOfferEntity> findByName(String name);
}
