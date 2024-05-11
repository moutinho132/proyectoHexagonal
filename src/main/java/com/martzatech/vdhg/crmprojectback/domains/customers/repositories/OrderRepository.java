package com.martzatech.vdhg.crmprojectback.domains.customers.repositories;

import com.martzatech.vdhg.crmprojectback.domains.chat.entities.FileEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.OrderEntity;
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
public interface OrderRepository extends JpaRepository<OrderEntity, Integer>,
    JpaSpecificationExecutor<OrderEntity> {

  @Override
  @Query(value = "select o from OrderEntity o where o.id = :id and o.deletedStatus.id=1")
  Optional<OrderEntity> findById(Integer id);

  @Query("select e from OrderEntity e where e.offer.id = :offerId")
  List<OrderEntity> findByOfferId(@Param("offerId") Integer offerId);

  @Modifying
  @Transactional
  @Query(value = "update OrderEntity p set p.deletedStatus.id=2 where p.id = :id")
  void deleteStatus(@Param("id") Integer id);
}
