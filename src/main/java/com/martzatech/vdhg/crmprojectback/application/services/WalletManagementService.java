package com.martzatech.vdhg.crmprojectback.application.services;

import com.martzatech.vdhg.crmprojectback.application.exceptions.BusinessRuleException;
import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.CustomerNotFoundException;
import com.martzatech.vdhg.crmprojectback.domains.commons.models.Person;
import com.martzatech.vdhg.crmprojectback.domains.wallet.models.Wallet;
import com.martzatech.vdhg.crmprojectback.domains.wallet.repositories.WalletRepository;
import com.martzatech.vdhg.crmprojectback.domains.wallet.services.WalletService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Slf4j
@Service
public class WalletManagementService {
    private final WalletService service;
    private final WalletRepository repository;

    @Transactional
    public Wallet save(final Wallet model) {
     if(Objects.isNull(model)){
         throw new BusinessRuleException("Cannot upload file in wallet objet null");
     }
        return service.save(buildWallet(model));
    }
    public List<Wallet> findByPerson(Person model , Pageable pageable){
       return service.findByPerson(model,pageable);
    }

    private static Wallet buildWallet(Wallet model) {
        return Wallet
                .builder()
                .file(model.getFile())
                .person(model.getPerson())
                .creationTime(model.getCreationTime())
                .build();
    }

    public void existsById(final Integer id) {
        if (!repository.existsById(id)) {
            throw new CustomerNotFoundException(id);
        }
    }
}
