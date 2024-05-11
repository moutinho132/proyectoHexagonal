package com.martzatech.vdhg.crmprojectback.domains.vendors.services;

import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.VendorContactNotFoundException;
import com.martzatech.vdhg.crmprojectback.domains.vendors.mapper.VendorContactMapper;
import com.martzatech.vdhg.crmprojectback.domains.vendors.models.VendorContact;
import com.martzatech.vdhg.crmprojectback.domains.vendors.repositories.VendorContactRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@Slf4j
@Service
public class VendorContactService {
    private VendorContactRepository vendorContactRepository;
    private VendorContactMapper vendorContactMapper;

    public VendorContact save(final VendorContact model) {
        if (Objects.requireNonNullElse(model.getId(), 0) != 0) {
            existsById(model.getId());
        }
        return vendorContactMapper.entityToModel(vendorContactRepository.save(vendorContactMapper.modelToEntity(model)));
    }

    //TODO:faltaria el findAll
    public VendorContact findById(final Integer id) {
        return vendorContactMapper.entityToModel(vendorContactRepository.findById(id).orElseThrow(() -> new VendorContactNotFoundException(id)));
    }

    public void deleteById(final Integer id) {
        existsById(id);

        vendorContactRepository.deleteById(id);
    }

    public void existsById(final Integer id) {
        if (!vendorContactRepository.existsById(id)) {
            throw new VendorContactNotFoundException(id);
        }
    }

    public Optional<VendorContact> findByName(final String name) {
        return vendorContactRepository.findByName(name).map(entity -> vendorContactMapper.entityToModel(entity));
    }
}
