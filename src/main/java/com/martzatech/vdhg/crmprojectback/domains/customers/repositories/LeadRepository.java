package com.martzatech.vdhg.crmprojectback.domains.customers.repositories;

import com.martzatech.vdhg.crmprojectback.domains.customers.entities.LeadEntity;
import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LeadRepository extends JpaRepository<LeadEntity, Integer>, JpaSpecificationExecutor<LeadEntity> {


  @Override
  @Query(value = "select l from LeadEntity l where l.id = :id and l.deletedStatus.id=1")
  Optional<LeadEntity> findById(Integer id);

  @Query("select l from LeadEntity l, PersonEntity p, EmailEntity e "
      + " where l.person.id=p.id "
      + "   and e.person.id=p.id "
      + "   and e.value=:email")
  List<LeadEntity> findByEmail(@Param("email") String email);
  @Modifying
  @Transactional
  @Query(value = "update LeadEntity p set p.deletedStatus.id=2 where p.id = :id")
  void deleteStatus(@Param("id") Integer id);
}
