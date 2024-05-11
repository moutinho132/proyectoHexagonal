package com.martzatech.vdhg.crmprojectback.domains.customers.services;

import com.martzatech.vdhg.crmprojectback.application.enums.DeletedStatusEnum;
import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.ProductNotFoundException;
import com.martzatech.vdhg.crmprojectback.application.helper.SpecificationHelper;
import com.martzatech.vdhg.crmprojectback.domains.customers.mappers.ProductMapper;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Product;
import com.martzatech.vdhg.crmprojectback.domains.customers.repositories.ProductRepository;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.ProductSpecification;
import com.martzatech.vdhg.crmprojectback.domains.vendors.mapper.VendorMapper;
import com.martzatech.vdhg.crmprojectback.domains.vendors.models.Vendor;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.impl.ProductMapperImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
@Service
public class ProductService {

    private ProductRepository repository;
    private ProductMapper mapper;
    private VendorMapper vendorMapper;

    public Product save(final Product model) {
        if (Objects.requireNonNullElse(model.getId(), 0) != 0) {
            existsById(model.getId());
        }
        return mapper.entityToModel(repository.save(mapper.modelToEntity(model)));
    }

    public Long count(final ProductSpecification specification) {
        return repository.count(SpecificationHelper.addSoftDeleteProduct(DeletedStatusEnum.ACTIVE.getId(), specification));
    }

    public List<Product> findAll(final ProductSpecification specification, final Pageable pageable) {

        return mapper
                .entitiesToModelList(repository
                        .findAll(SpecificationHelper
                                .addSoftDeleteProduct(DeletedStatusEnum.ACTIVE.getId(), specification), pageable)
                        .toList().stream().map(ProductMapperImpl::buildProductEntity).collect(Collectors.toList()));
    }

    public Product findById(final Integer id) {
        return mapper.entityToModel(repository.findById(id).orElseThrow(() -> new ProductNotFoundException(id)));
    }

    public void deleteById(final Integer id) {
        existsById(id);

        repository.deleteById(id);
    }

    public void deleteStatus(final Integer id) {
        existsById(id);

        repository.deleteStatus(id);
    }

    public void deleteByStatusAndId(final Integer id) {
        existsById(id);

        repository.deleteById(id);
    }

    public void existsById(final Integer id) {
        if (!repository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
    }

    public Product findByName(final String name) {
        return repository.findByName(name).map(mapper::entityToModel).orElse(null);
    }

    public List<Product> findProductsBySubCategory(final Integer subCategoryId) {
        return repository.findBySubCategories_Id(subCategoryId).stream().map(mapper::entityToModel).toList();
    }
    //.sorted(Comparator.comparing(chatRoom -> Objects.isNull(chatRoom.getLastMessage())
    //                                    ? chatRoom.getCreationTime() : chatRoom.getLastMessage().getCreationTime(),
    //                            Comparator.reverseOrder()))
    public List<Product> findByVendor(final Vendor model) {
        return repository.findByVendor(vendorMapper.modelToEntity(model))
                .stream()
                .map(mapper::entityToModel).sorted(Comparator.comparing(Product::getId).reversed())
                .toList();
    }

}
