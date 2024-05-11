package com.martzatech.vdhg.crmprojectback.domains.wallet.repositories;

import com.martzatech.vdhg.crmprojectback.domains.commons.entities.PersonEntity;
import com.martzatech.vdhg.crmprojectback.domains.wallet.entities.WalletEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletRepository  extends JpaRepository<WalletEntity, Integer>,
        JpaSpecificationExecutor<WalletEntity> {
  List<WalletEntity> findByPerson(@Param("person") PersonEntity person, Pageable pageable);
}
