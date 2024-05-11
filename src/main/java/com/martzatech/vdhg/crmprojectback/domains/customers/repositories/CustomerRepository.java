package com.martzatech.vdhg.crmprojectback.domains.customers.repositories;

import com.martzatech.vdhg.crmprojectback.domains.customers.entities.CustomerEntity;
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
public interface CustomerRepository extends JpaRepository<CustomerEntity, Integer>,
    JpaSpecificationExecutor<CustomerEntity> {
  @Modifying
  @Transactional
  @Query(value = "DELETE from CustomerEntity c where c.id = :id")
  void deleteById(@Param("id") Integer id);
  @Modifying
  @Transactional
  @Query(value = "update CustomerEntity c set c.deletedStatus.id=2 where c.id = :id")
  void deleteByStatus(@Param("id") Integer id);

  @Override
  @Query(value = "select c from CustomerEntity c where c.id=:id and c.deletedStatus.id=1")
  Optional<CustomerEntity> findById(Integer id);

  @Query("select c from CustomerEntity c where c.lead.id=:leadId")
  Optional<CustomerEntity> findByLeadId(@Param("leadId") Integer leadId);

  @Query("select c from CustomerEntity c, PersonEntity p, EmailEntity e "
      + " where c.person.id=p.id "
      + "   and e.person.id=p.id "
      + "   and e.value=:email")
  List<CustomerEntity> findByEmail(@Param("email") String email);
}
