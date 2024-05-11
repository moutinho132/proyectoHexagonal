package com.martzatech.vdhg.crmprojectback.domains.vendors.repositories;

import com.martzatech.vdhg.crmprojectback.domains.vendors.entities.VendorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VendorRepository extends JpaRepository<VendorEntity, Integer>, JpaSpecificationExecutor<VendorEntity> {
    Optional<VendorEntity> findByName(String name);

}
