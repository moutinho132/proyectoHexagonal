package com.martzatech.vdhg.crmprojectback.domains.customers.repositories;

import com.martzatech.vdhg.crmprojectback.domains.customers.entities.OfferEntity;
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
public interface OfferRepository extends JpaRepository<OfferEntity, Integer>,
    JpaSpecificationExecutor<OfferEntity> {

  Optional<OfferEntity> findByName(String name);
  @Query(value = "select max(e.number) from OfferEntity e ")
  Integer getMaxNumber();

  @Query(value = "select max(e.version) from OfferEntity e where e.number=:number")
  Integer getMaxVersion(@Param("number") Integer number);

  boolean existsByNumber(Integer number);

  @Query("select e from OfferEntity e where  e.number=:number")
  List<OfferEntity> findByNumber(Integer number);

  @Query("select e from OfferEntity e where  e.number=:number and e.customer.id=:customerId and e.globalStatus=1 and e.status=5")
  List<OfferEntity> findByNumberAndStatus(Integer number,Integer customerId);

  @Query("select e from OfferEntity e where  e.number=:number and e.customer.id=:customerId")
  List<OfferEntity> findByNumberOrder(Integer number,Integer customerId);




  @Query("select e from OfferEntity e where  e.customer.id=:customerId and e.deletedStatus.id=1")
  List<OfferEntity> findByCustomerId(Integer customerId);

  @Query("select e from OfferEntity e where e.preOffer.id = :preOfferId")
  List<OfferEntity> findByPreOfferId(@Param("preOfferId") Integer preOfferId);

  @Modifying
  @Query(value = "update OfferEntity o set o.deletedStatus.id=2 where o.id = :id")
  @Transactional
  void deleteStatus(@Param("id") Integer id);
}
