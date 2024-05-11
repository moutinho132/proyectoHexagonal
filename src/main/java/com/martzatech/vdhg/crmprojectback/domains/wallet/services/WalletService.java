package com.martzatech.vdhg.crmprojectback.domains.wallet.services;

import com.martzatech.vdhg.crmprojectback.domains.commons.mappers.PersonMapper;
import com.martzatech.vdhg.crmprojectback.domains.commons.models.Person;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.WalletSpecification;
import com.martzatech.vdhg.crmprojectback.domains.wallet.mapper.WalletMapper;
import com.martzatech.vdhg.crmprojectback.domains.wallet.models.Wallet;
import com.martzatech.vdhg.crmprojectback.domains.wallet.repositories.WalletRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class WalletService {
    private WalletRepository repository;
    private WalletMapper mapper;
    private PersonMapper personMapper;
    public Wallet save(final Wallet model) {
        return mapper.entityToModel(repository.save(mapper.modelToEntity(model)));
    }

    public Long count(final WalletSpecification specification) {
        return repository.count(specification);
    }

    public List<Wallet> findByPerson(final Person model , Pageable pageable) {
       return  mapper.entityToModelList(repository.findByPerson(personMapper.modelToEntity(model),pageable));
    }

}
