package com.martzatech.vdhg.crmprojectback.domains.customers.repositories;

import com.martzatech.vdhg.crmprojectback.domains.customers.entities.AssociatedEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.GroupAccountEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssociatedRepository extends JpaRepository<AssociatedEntity, Integer> {

    @Modifying
    @Transactional
    @Query(value = "DELETE from AssociatedEntity c where c.id = :id")
    void deleteById(@Param("id") Integer id);
    Optional<AssociatedEntity> findByEmail(String email);

    @Modifying
    @Query(value = "update AssociatedEntity e set e.mainContact = false where e.groupAccount.id = :groupAccountId")
    void updateMainContactToFalseByGroupAccountId(@Param("groupAccountId") Integer groupAccountId);

    List<AssociatedEntity> findByGroupAccount(GroupAccountEntity accountEntity);

}
