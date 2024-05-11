package com.martzatech.vdhg.crmprojectback.domains.customers.repositories;

import com.martzatech.vdhg.crmprojectback.domains.customers.entities.GroupAccountEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupAccountRepository extends JpaRepository<GroupAccountEntity, Integer>,
    JpaSpecificationExecutor<GroupAccountEntity> {

  @Query(value = "select e from GroupAccountEntity e where e.owner.id = :customerId")
  Optional<GroupAccountEntity> findByCustomerId(@Param("customerId") Integer customerId);

  GroupAccountEntity findByEmail(String email);

  GroupAccountEntity findByName(String name);

  @Override
  @Query(value = "select g from GroupAccountEntity g where g.id = :id and g.status.id=1")
  Optional<GroupAccountEntity> findById(Integer id);


  @Modifying
  @Transactional
  @Query(value = "DELETE from GroupAccountEntity c where c.id = :id")
  void deleteById(@Param("id") Integer id);

  @Modifying
  @Transactional
  @Query(value = "update GroupAccountEntity o set o.status.id=2 where o.id = :id")
  void deleteStatus(@Param("id") Integer id);
}
