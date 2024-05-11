package com.martzatech.vdhg.crmprojectback.domains.customers.repositories;

import com.martzatech.vdhg.crmprojectback.domains.customers.entities.ProductEntity;
import com.martzatech.vdhg.crmprojectback.domains.vendors.entities.VendorEntity;
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
public interface ProductRepository extends JpaRepository<ProductEntity, Integer>,
        JpaSpecificationExecutor<ProductEntity> {
    @Modifying
    @Transactional
    @Query(value = "update ProductEntity p set p.status.id=2 where p.id = :id")
    void deleteStatus(@Param("id") Integer id);

    Optional<ProductEntity> findByName(String name);
    List<ProductEntity> findByVendor(final VendorEntity entity);

    List<ProductEntity> findBySubCategories_Id(@Param("subCategoryId") Integer subCategoryId);
}
