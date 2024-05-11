package com.martzatech.vdhg.crmprojectback.domains.customers.services;

import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.PreOfferNotFoundException;
import com.martzatech.vdhg.crmprojectback.application.helper.BockingProductHelper;
import com.martzatech.vdhg.crmprojectback.domains.chat.services.FileService;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.PreOfferEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.enums.OfferStatusEnum;
import com.martzatech.vdhg.crmprojectback.domains.customers.mappers.BockingProductMapper;
import com.martzatech.vdhg.crmprojectback.domains.customers.mappers.PreOfferMapper;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.BockingProduct;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.PreOffer;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Product;
import com.martzatech.vdhg.crmprojectback.domains.customers.repositories.BockingProductRepository;
import com.martzatech.vdhg.crmprojectback.domains.customers.repositories.PreOfferRepository;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.PreOfferSpecification;
import com.martzatech.vdhg.crmprojectback.domains.vendors.models.Vendor;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.FileApiMapper;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@Slf4j
@Service
public class PreOfferService {

    private final PreOfferRepository repository;
    private final PreOfferMapper mapper;
    private final FileApiMapper fileApiMapper;
    private final FileService fileService;
    private final BockingProductMapper bockingProductMapper;
    private final BockingProductRepository bockingProductRepository;
    private final FileApiMapper fileMapper;
    private final ProductService productService;

    private static void assignedBuildPreOfferListResult(PreOffer preOffer, List<PreOffer> preOffersResult,
                                                        List<BockingProduct> productPrices) {
        preOffersResult.add(PreOffer
                .builder()
                .currency(preOffer.getCurrency())
                .name(preOffer.getName())
                .number(preOffer.getNumber())
                .id(preOffer.getId())
                .products(productPrices)
                .description(preOffer.getDescription())
                .discount(preOffer.getDiscount())
                .total(getTotal(preOffer))
                .subtotal(getTotal(preOffer))
                .textToClient(preOffer.getTextToClient())
                .defaultExpiration(preOffer.getDefaultExpiration())
                .creationTime(preOffer.getCreationTime())
                .creationUser(preOffer.getCreationUser())
                .modificationTime(preOffer.getModificationTime())
                .modificationUser(preOffer.getModificationUser())
                .paymentRequired(preOffer.getPaymentRequired())
                .active(preOffer.getActive())
                .expirationTime(preOffer.getExpirationTime())
                .globalStatus(preOffer.getGlobalStatus())
                .pdfUrl(preOffer.getPdfUrl())
                .version(preOffer.getVersion())
                .status(preOffer.getStatus())
                .build());
    }

    private PreOffer assignedBuildPreOfferResult(PreOffer offer,
                                                 List<BockingProduct> productPrices) {
       return PreOffer
                .builder()
                .currency(offer.getCurrency())
                .name(offer.getName())
                .number(offer.getNumber())
                .id(offer.getId())
                .products(productPrices)
                .description(offer.getDescription())
                .discount(offer.getDiscount())
                .total(getTotal(offer))
                .subtotal(getTotal(offer))
                .textToClient(offer.getTextToClient())
                .defaultExpiration(offer.getDefaultExpiration())
                .creationTime(offer.getCreationTime())
                .creationUser(offer.getCreationUser())
                .modificationTime(offer.getModificationTime())
                .modificationUser(offer.getModificationUser())
                .paymentRequired(offer.getPaymentRequired())
                .active(offer.getActive())
                .expirationTime(offer.getExpirationTime())
                .globalStatus(offer.getGlobalStatus())
                .pdfUrl(offer.getPdfUrl())
                .version(offer.getVersion())
                .status(offer.getStatus())
                               .build();
    }


    private static BigDecimal getSubtotal(final PreOffer preOffer) {
       /* if (Objects.nonNull(preOffer.getSubtotal())) {
            return preOffer.getSubtotal();
        }*/
        return plusProductPrices(preOffer) == null ? BigDecimal.ZERO : plusProductPrices(preOffer);
    }

    private static BigDecimal getTotal(final PreOffer preOffer) {
        return plusProductTotal(preOffer) == null ? BigDecimal.ZERO :
                plusProductTotal(preOffer).setScale(2, RoundingMode.HALF_EVEN);
    }

    private static BigDecimal plusProductPrices(final PreOffer preOffer) {
        return !CollectionUtils.isEmpty(preOffer.getProducts()) ? preOffer.getProducts().stream()
                .map(bockingProduct -> {
                    if (Boolean.TRUE.equals(bockingProduct.getDefaultBasePrice())) {
                        return bockingProduct.getProduct().getBasePrice();
                    } else {
                        return bockingProduct.getBasePrice();
                    }
                })
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add) : null;
    }


    private static BigDecimal plusProductTotal(final PreOffer preOffer) {
        return !CollectionUtils.isEmpty(preOffer.getProducts()) ? preOffer.getProducts().stream()
                .map(bockingProduct -> {
                    if (bockingProduct.getRequiresPayment() == Boolean.TRUE) {
                        return bockingProduct.getTotalWithVat();
                    } else {
                        return BigDecimal.ZERO; // Si RequiresPayment es false, devuelve 0.
                    }
                })
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add) : BigDecimal.ZERO;
    }


    private static Specification<PreOfferEntity> getSpecByLastVersion(
            final PreOfferSpecification specification) {
        return (root, query, criteriaBuilder) -> {
            final Subquery<Integer> subQuery = query.subquery(Integer.class);
            final Root<PreOfferEntity> subRoot = subQuery.from(PreOfferEntity.class);
            subQuery
                    .select(criteriaBuilder.max(subRoot.get("version")))
                    .where(criteriaBuilder.equal(subRoot.get("number"), root.get("number")));
            return Objects.nonNull(specification)
                    ? criteriaBuilder
                    .and(
                            criteriaBuilder.equal(root.get("version"), subQuery),
                            specification.toPredicate(root, query, criteriaBuilder)
                    )
                    : criteriaBuilder
                    .and(
                            criteriaBuilder.equal(root.get("version"), subQuery)
                    );
        };
    }

    public PreOffer save(final PreOffer model) {
        if (Objects.requireNonNullElse(model.getId(), 0) != 0) {
            existsById(model.getId());
        }

        return mapper.entityToModel(repository.save(mapper.modelToEntity(model)));
    }

    public List<PreOffer> findAll(final PreOfferSpecification specification, final Pageable pageable) {
        final List<PreOffer> preOffersResult = new ArrayList<>();
        final Specification<PreOfferEntity> byLastVersion = getSpecByLastVersion(specification);
        List<PreOffer> preOffers = mapper.entitiesToModelList(
                repository
                        .findAll(
                                byLastVersion,
                                pageable
                        )
                        .toList()
        );
        preOffers.forEach(preOffer -> {
            List<BockingProduct> productPrices = getProductPriceList(preOffer);//Lista producto

            assignedBuildPreOfferListResult(preOffer, preOffersResult, productPrices);
        });

        return preOffersResult;
    }


    private List<BockingProduct> getProductPriceList(PreOffer preOfferVersion) {
        return buildBockingProductUpdate(preOfferVersion);
    }


    public List<BockingProduct> buildBockingProductUpdate(PreOffer preOffer) {
        List<BockingProduct> bockingProduct = new ArrayList<>();
        if (preOffer.getStatus().equals(OfferStatusEnum.OPEN)) {
            if (!CollectionUtils.isEmpty(preOffer.getProducts())) {
                preOffer.getProducts().stream().forEach(productPrice -> {
                    var bockingProductUpdate = getBockingProductUpdate(productPrice, preOffer);
                    bockingProduct.add(bockingProductUpdate);
                });
            }
            if (!CollectionUtils.isEmpty(bockingProduct)) {
                preOffer.withProducts(bockingProduct);
                save(preOffer.withTotal(getTotal(preOffer))
                        .withSubtotal(getTotal(preOffer))
                        .withExpirationTime(getExpirationTime(preOffer)));
            }
        } else {
            if (!CollectionUtils.isEmpty(preOffer.getProducts())) {
                preOffer.getProducts().stream().forEach(productPrice -> {
                    var bockingProductResult = getBockingProductResult(productPrice, preOffer);
                    bockingProduct.add(bockingProductResult);
                });
            }
        }
        return bockingProduct;
    }

    private static LocalDateTime getExpirationTime(PreOffer preOffer) {
        if (preOffer == null) {
            return null;
        }

        if (preOffer.getDefaultExpiration() == Boolean.TRUE) {
            // Si defaultExpiration es true, calcula el mínimo de availabilityTo
            if (!CollectionUtils.isEmpty(preOffer.getProducts())) {
                return preOffer.getProducts().stream()
                        .map(product -> product.getProduct())
                        .filter(product -> product != null && product.getAvailabilityTo() != null)
                        .map(product -> product.getAvailabilityTo())
                        .min(LocalDateTime::compareTo)
                        .orElse(null);
            }
        } else {
            return preOffer.getExpirationTime();
        }

        return null; // En caso de que ninguna condición sea verdadera
    }


    private BockingProduct getBockingProductResult(final BockingProduct productPrice, final PreOffer preOffer) {
        return BockingProduct
                .builder()
                .id(productPrice.getId())
                .productName(productPrice.getProduct().getName())
                .vendor(Objects.nonNull(productPrice.getProduct().getVendor())
                        ? Vendor.builder()
                        .id(productPrice.getProduct().getVendor().getVendorId())
                        .name(Objects.nonNull(productPrice.getProduct().getVendor().getName())?productPrice.getProduct().getVendor().getName():null)
                        .commission(Objects.nonNull(productPrice.getProduct().getVendor().getCommission())?productPrice.getProduct().getVendor().getCommission():null)
                        .vat(Objects.nonNull(productPrice.getProduct().getVendor().getVat())?productPrice.getProduct().getVendor().getVat():null).build(): null)
                .preOffer(preOffer)
                .productId(productPrice.getProduct().getId())
                .paymentDetails(productPrice.getPaymentDetails())
                .marketing(productPrice.getMarketing())
                .vatPercentage(productPrice.getVatPercentage())
                .vatValue(productPrice.getVatValue())
                .paymentReference(productPrice.getPaymentReference())
                .availabilityTo(productPrice.getAvailabilityTo())
                .defaultAvailabilityTo(productPrice.getDefaultAvailabilityTo())
                .defaultPaymentDetails(productPrice.getDefaultPaymentDetails())
                .defaultDescription(productPrice.getDefaultDescription())
                .description(productPrice.getDescription())
                .commissionPercentage(productPrice.getCommissionPercentage())
                .commissionValue(productPrice.getCommissionValue())
                .defaultVat(productPrice.getDefaultVat())
                .basePrice(productPrice.getBasePrice())
                .totalWithCommission(productPrice.getTotalWithCommission())
                .totalWithVat(productPrice.getTotalWithVat())
                .defaultMarketing(productPrice.getDefaultMarketing())
                .defaultCommission(productPrice.getDefaultCommission())
                .defaultBasePrice(productPrice.getDefaultBasePrice())
                .showPrice(productPrice.getShowPrice())
                .requiresPayment(productPrice.getRequiresPayment())
                .availabilityFrom(productPrice.getAvailabilityFrom())
                .defaultAvailabilityFrom(productPrice.getDefaultAvailabilityFrom())
                .product(Objects.nonNull(productPrice.getProduct()) ? Product.builder().id(productPrice.getProduct().getId()).build() : null)
                .files(Objects.nonNull(productPrice.getId()) ? fileApiMapper
                        .modelsToResponseList(fileService
                                .getFileProductPreOffer(productPrice.getId())) : null)
                .build();
    }

    private BockingProduct getBockingProductUpdate(final BockingProduct productPrice, final PreOffer preOffer) {
        return bockingProductMapper.entityToModel(bockingProductRepository.save(bockingProductMapper.modelToEntity(BockingProduct
                .builder()
                .id(productPrice.getId())
                .productName(productPrice.getProduct().getName())
                        .vendor(Objects.nonNull(productPrice.getProduct().getVendor())
                                ? Vendor.builder().id( productPrice.getProduct().getVendor().getVendorId())
                                .name( productPrice.getProduct().getVendor().getName())
                                .vat(productPrice.getProduct().getVendor().getVat())
                                .commission(productPrice.getProduct().getVendor().getCommission()).build():null)
                .preOffer(preOffer)
                .productId(productPrice.getProduct().getId())
                .showPrice(productPrice.getShowPrice())
                .defaultPaymentDetails(productPrice.getDefaultPaymentDetails())
                .paymentDetails(productPrice.getPaymentDetails())
                .defaultBasePrice(productPrice.getDefaultBasePrice())
                .basePrice(BockingProductHelper.getBasePrice(productPrice))
                .defaultCommission(productPrice.getDefaultCommission())
                .commissionPercentage(BockingProductHelper.getCommission(productPrice))
                .commissionValue(BockingProductHelper.getCommissionValue(productPrice))
                .totalWithCommission(BockingProductHelper.getResultTotalWithCommission(productPrice,productPrice.getProduct()))
                .vatPercentage(BockingProductHelper.getVat(productPrice))
                .vatValue(BockingProductHelper.getVatValue(productPrice))
                .totalWithVat(BockingProductHelper.getResultTotalWithVat(productPrice))
                .paymentReference(productPrice.getPaymentReference())
                .defaultDescription(productPrice.getDefaultDescription())
                .description(BockingProductHelper.getDescription(productPrice))
                .defaultVat(productPrice.getDefaultVat())
                .defaultMarketing(productPrice.getDefaultMarketing())
                .marketing(BockingProductHelper.getMarketing(productPrice))
                .requiresPayment(productPrice.getRequiresPayment())
                .defaultAvailabilityFrom(productPrice.getDefaultAvailabilityFrom())
                .availabilityFrom(BockingProductHelper.getAvailabilityFrom(productPrice))
                .defaultAvailabilityTo(productPrice.getDefaultAvailabilityTo())
                .availabilityTo(BockingProductHelper.getAvailabilityTo(productPrice))
                .product(Objects.nonNull(productPrice.getProduct()) ? Product.builder().id(productPrice.getProduct().getId()).build() : null)
                .files(Objects.nonNull(productPrice.getId()) ? fileApiMapper
                        .modelsToResponseList(fileService
                                .getFileProductPreOffer(productPrice.getId())) : null)
                .build())));
    }

    public PreOffer findById(final Integer id) {
        log.info("findById PreOffer id :{}",id);

        final Specification<PreOfferEntity> specificationById = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.<Integer>get("id"), id
                );
        final List<PreOfferEntity> findAll = repository.findAll(specificationById);
        if (CollectionUtils.isEmpty(findAll)) {
            throw new PreOfferNotFoundException(id);
        }
        PreOffer preOffer = mapper.entityToModel(findAll.get(0));
        List<BockingProduct> productPrices = getProductPriceList(preOffer);//Lista producto
        return  assignedBuildPreOfferResult(preOffer,productPrices);
    }

    public void deleteById(final Integer id) {
        log.info("Delete PreOffer Id:{}",id);
        existsById(id);

        repository.deleteById(id);
    }

    public void existsById(final Integer id) {
        if (!repository.existsById(id)) {
            throw new PreOfferNotFoundException(id);
        }
    }

    public Integer getCurrentNumber() {
        return Objects.requireNonNullElse(repository.getMaxNumber(), 0) + 1;
    }

    public Integer getCurrentVersion(final Integer number) {
        return Objects.requireNonNullElse(repository.getMaxVersion(number), 0) + 1;
    }

    public boolean existsByNumber(final Integer number) {
        return repository.existsByNumber(number);
    }

    public Long count(final PreOfferSpecification specification) {
        final Specification<PreOfferEntity> byLastVersion = getSpecByLastVersion(specification);
        return repository.count(byLastVersion);
    }

    public List<PreOffer> findByNumber(final Integer number) {
        final List<PreOffer> preOffersResult = new ArrayList<>();
        mapper.entitiesToModelList(repository.findByNumber(number)).forEach(preOffer -> {
            log.info("FindByNumber PreOffer for id: {}",preOffer.getId());
            List<BockingProduct> productPrices = getProductPriceList(preOffer);
            assignedBuildPreOfferListResult(preOffer, preOffersResult, productPrices);
        });
        return preOffersResult;
    }

    public PreOffer findByName(final String name) {
        Optional<PreOfferEntity> preOfferEntity = repository.findByName(name);
        PreOfferEntity preOffer = null;
        if (preOfferEntity.isPresent()) {
            preOffer = preOfferEntity.get();
        }
        return mapper.entityToModel(preOffer);
    }


}
