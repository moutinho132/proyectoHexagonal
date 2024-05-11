package com.martzatech.vdhg.crmprojectback.domains.vendors.repositories;

import com.martzatech.vdhg.crmprojectback.domains.vendors.entities.VendorLocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VendorLocationRepository extends JpaRepository<VendorLocationEntity, Integer> {
    Optional<VendorLocationEntity> findByName(String name);
}
