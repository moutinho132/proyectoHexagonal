package com.martzatech.vdhg.crmprojectback.domains.customers.services;

import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.ProductLocationNotFoundException;
import com.martzatech.vdhg.crmprojectback.domains.customers.mappers.ProductLocationMapper;
import com.martzatech.vdhg.crmprojectback.domains.customers.mappers.ProductMapper;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Product;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.ProductLocation;
import com.martzatech.vdhg.crmprojectback.domains.customers.repositories.ProductLocationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Slf4j
@Service
public class ProductLocationService {

    private ProductLocationRepository repository;
    private ProductLocationMapper mapper;
    private ProductMapper productMapper;

    public ProductLocation save(final ProductLocation model) {
        if (Objects.requireNonNullElse(model.getId(), 0) != 0) {
            existsById(model.getId());
        }
        return mapper.entityToModel(repository.save(mapper.modelToEntity(model)));
    }

    public void deleteById(final Integer id) {
        existsById(id);

        repository.deleteById(id);
    }

    public ProductLocation findById(final Integer id) {
        return mapper.entityToModel(repository.findById(id).orElseThrow(() -> new ProductLocationNotFoundException(id)));
    }



    public void existsById(final Integer id) {
        if (!repository.existsById(id)) {
            throw new ProductLocationNotFoundException(id);
        }
    }
}
