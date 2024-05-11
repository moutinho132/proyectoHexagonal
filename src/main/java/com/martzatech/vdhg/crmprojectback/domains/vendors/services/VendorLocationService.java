package com.martzatech.vdhg.crmprojectback.domains.vendors.services;

import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.VendorLocationNotFoundException;
import com.martzatech.vdhg.crmprojectback.domains.vendors.mapper.VendorLocationMapper;
import com.martzatech.vdhg.crmprojectback.domains.vendors.models.VendorLocation;
import com.martzatech.vdhg.crmprojectback.domains.vendors.repositories.VendorLocationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@Slf4j
@Service
public class VendorLocationService {
    private VendorLocationRepository vendorLocationRepository;
    private VendorLocationMapper locationMapper;

    public VendorLocation save(final VendorLocation model) {
        if (Objects.requireNonNullElse(model.getId(), 0) != 0) {
            existsById(model.getId());
        }
        return locationMapper.entityToModel(vendorLocationRepository.save(locationMapper.modelToEntity(model)));
    }

    //TODO:faltaria el findAll
    public VendorLocation findById(final Integer id) {
        return locationMapper.entityToModel(vendorLocationRepository.findById(id).orElseThrow(() -> new VendorLocationNotFoundException(id)));
    }

    public void deleteById(final Integer id) {
        existsById(id);

        vendorLocationRepository.deleteById(id);
    }

    public void existsById(final Integer id) {
        if (!vendorLocationRepository.existsById(id)) {
            throw new VendorLocationNotFoundException(id);
        }
    }

    public Optional<VendorLocation> findByName(final String name) {
        return vendorLocationRepository.findByName(name).map(entity -> locationMapper.entityToModel(entity));
    }
}
