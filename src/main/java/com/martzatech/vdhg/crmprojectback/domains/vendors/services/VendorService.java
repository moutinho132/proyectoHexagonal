package com.martzatech.vdhg.crmprojectback.domains.vendors.services;

import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.VendorNotFoundException;
import com.martzatech.vdhg.crmprojectback.domains.vendors.mapper.VendorMapper;
import com.martzatech.vdhg.crmprojectback.domains.vendors.models.Vendor;
import com.martzatech.vdhg.crmprojectback.domains.vendors.repositories.VendorRepository;
import com.martzatech.vdhg.crmprojectback.domains.vendors.specifications.VendorSpecification;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@Slf4j
@Service
public class VendorService {
    private VendorRepository vendorRepository;
    private VendorMapper vendorMapper;

    public Vendor save(final Vendor model) {
        if (Objects.requireNonNullElse(model.getId(), 0) != 0) {
            existsById(model.getId());
        }
        return vendorMapper.entityToModel(vendorRepository.save(vendorMapper.modelToEntity(model)));
    }

    public List<Vendor> findAll(final VendorSpecification specification, final Pageable pageable) {
        return vendorMapper.entitiesToModelList(vendorRepository
                        .findAll(specification, pageable)
                        .toList());
    }

    public Long count(final VendorSpecification specification) {
        return vendorRepository.count(specification);
    }

    public Vendor findById(final Integer id) {
        return vendorMapper.entityToModel(vendorRepository.findById(id).orElseThrow(() -> new VendorNotFoundException(id)));
    }

    public void deleteById(final Integer id) {
        existsById(id);

        vendorRepository.deleteById(id);
    }

    public void existsById(final Integer id) {
        if (!vendorRepository.existsById(id)) {
            throw new VendorNotFoundException(id);
        }
    }

    public Optional<Vendor> findByName(final String name) {
        return vendorRepository.findByName(name).map(entity -> vendorMapper.entityToModel(entity));
    }
}
