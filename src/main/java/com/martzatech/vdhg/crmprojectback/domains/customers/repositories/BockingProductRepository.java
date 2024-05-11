package com.martzatech.vdhg.crmprojectback.domains.customers.repositories;

import com.martzatech.vdhg.crmprojectback.domains.customers.entities.BockingProductEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.OfferEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.PreOfferEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BockingProductRepository extends JpaRepository<BockingProductEntity, Integer> {
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM BockingProductEntity pp WHERE pp.id = :id")
    void delete(@Param("id") Integer id);

    List<BockingProductEntity> findByOffer(final OfferEntity offer);

    List<BockingProductEntity> findByPreOffer(final PreOfferEntity preOffer);
}
