package com.martzatech.vdhg.crmprojectback.domains.vendors.repositories;

import com.martzatech.vdhg.crmprojectback.domains.vendors.entities.VendorContactEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VendorContactRepository extends JpaRepository<VendorContactEntity, Integer> {
    Optional<VendorContactEntity> findByName(String name);
}
