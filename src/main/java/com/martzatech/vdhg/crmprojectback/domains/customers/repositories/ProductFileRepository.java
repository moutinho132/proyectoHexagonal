package com.martzatech.vdhg.crmprojectback.domains.customers.repositories;

import com.martzatech.vdhg.crmprojectback.domains.customers.entities.ProductEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.ProductFileEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductFileRepository extends JpaRepository<ProductFileEntity, Integer> {
    @Modifying
    @Transactional
    @Query(value = "update ProductFileEntity p set p.status.id=2 where p.id = :id")
    void deleteStatus(@Param("id") Integer id);
    List<ProductFileEntity> findByProduct(ProductEntity product);
}
