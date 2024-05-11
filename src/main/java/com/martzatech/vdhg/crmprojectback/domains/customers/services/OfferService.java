package com.martzatech.vdhg.crmprojectback.domains.customers.services;

import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.OfferNotFoundException;
import com.martzatech.vdhg.crmprojectback.application.helper.BockingProductHelper;
import com.martzatech.vdhg.crmprojectback.domains.chat.services.FileService;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.OfferEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.enums.OfferStatusEnum;
import com.martzatech.vdhg.crmprojectback.domains.customers.mappers.BockingProductMapper;
import com.martzatech.vdhg.crmprojectback.domains.customers.mappers.OfferMapper;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.BockingProduct;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Offer;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.PreOffer;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Product;
import com.martzatech.vdhg.crmprojectback.domains.customers.repositories.BockingProductRepository;
import com.martzatech.vdhg.crmprojectback.domains.customers.repositories.OfferRepository;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.OfferSpecification;
import com.martzatech.vdhg.crmprojectback.domains.vendors.models.Vendor;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.FileApiMapper;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
@Service
public class OfferService {

    private OfferRepository repository;
    private OfferMapper mapper;
    private FileService fileService;
    private FileApiMapper fileApiMapper;
    private ProductService productService;
    private final BockingProductMapper bockingProductMapper;
    private final BockingProductRepository bockingProductRepository;

    public Offer save(final Offer model) {
        if (Objects.requireNonNullElse(model.getId(), 0) != 0) {
            log.info("Save Offer Update: ID {}", model.getId());
            existsById(model.getId());
        }

        return mapper.entityToModel(repository.save(mapper.modelToEntity(model)));
    }

    @Transactional
    public List<Offer> findByNumber(final Integer number) {
        final List<Offer> preOffersResult = new ArrayList<>();

        if (CollectionUtils.isEmpty(repository.findByNumber(number))) {
            throw new OfferNotFoundException(number);
        }

        mapper.entitiesToModelList(repository.findByNumber(number)).stream()
                .collect(Collectors.toList()).forEach(preOffer -> {
                    List<BockingProduct> productPrices = getProductPriceList(preOffer);
                    assignedBuildOfferResult(preOffer, preOffersResult, productPrices);
                });
        return preOffersResult;
    }

    public List<Offer> findByCustomerId(final Integer customerId) {
        return mapper.entitiesToModelList(repository.findByCustomerId(customerId))
                .stream()
                .collect(Collectors.toList());
    }
    @Transactional
    public List<Offer> findAll(final OfferSpecification specification, final Pageable pageable) {
        final Specification<OfferEntity> byLastVersion = getSpecByLastVersion(specification);//.and(addSoftDeleteOffer(DeletedStatusEnum.ACTIVE.getId(), specification))
        final List<Offer> OffersResult = new ArrayList<>();

        List<Offer> offers = mapper.entitiesToModelList(
                repository
                        .findAll(
                                byLastVersion,
                                pageable)
                        .toList());

        offers.forEach(offer -> {
            List<BockingProduct> productPrices = getProductPriceList(offer);

            assignedBuildOfferResult(offer, OffersResult, productPrices);
        });
        return OffersResult;
    }

    public Offer findByName(final String name) {
        Optional<OfferEntity> offerEntity = repository.findByName(name);
        OfferEntity offer = null;
        if (offerEntity.isPresent()) {
            offer = offerEntity.get();
        }
        return mapper.entityToModel(offer);
    }

    private List<BockingProduct> getProductPriceList(Offer offer) {
        return buildBockingProductUpdate(offer);
    }

    public List<BockingProduct> buildBockingProductUpdate(final Offer offer) {
        List<BockingProduct> bockingProduct = new ArrayList<>();
        if (offer.getStatus().equals(OfferStatusEnum.OPEN)) {
            if (!CollectionUtils.isEmpty(offer.getProducts())) {
                offer.getProducts().stream().forEach(productPrice -> {
                    var bockingProductUpdate = getBockingProductUpdate(productPrice, offer);
                    bockingProduct.add(bockingProductUpdate);
                });
            }
            if (!CollectionUtils.isEmpty(bockingProduct)) {
                offer.withProducts(bockingProduct);
                save(offer.withTotal(getTotal(offer))
                        .withSubtotal(getTotal(offer))
                        .withExpirationTime(getExpirationTime(offer)));
            }
        } else {
            if (!CollectionUtils.isEmpty(offer.getProducts())) {
                offer.getProducts().stream().forEach(productPrice -> {
                    var bockingProductResult = getBockingProductResult(productPrice, offer);
                    bockingProduct.add(bockingProductResult);
                });
            }
        }
        return bockingProduct;
    }

    private static LocalDateTime getExpirationTime(Offer offer) {
        if (offer == null) {
            return null;
        }

        if (offer.getDefaultExpiration() == Boolean.TRUE) {
            // Si defaultExpiration es true, calcula el mínimo de availabilityTo
            if (!CollectionUtils.isEmpty(offer.getProducts())) {
                return offer.getProducts().stream()
                        .map(product -> product.getProduct())
                        .filter(product -> product != null && product.getAvailabilityTo() != null)
                        .map(product -> product.getAvailabilityTo())
                        .min(LocalDateTime::compareTo)
                        .orElse(null);
            }
        } else {
            return offer.getExpirationTime();
        }

        return null; // En caso de que ninguna condición sea verdadera
    }


    private BockingProduct getBockingProductResult(final BockingProduct productPrice, final Offer offer) {
        return BockingProduct
                .builder()
                .id(productPrice.getId())
                .productName(productPrice.getProduct().getName())
                .vendor(Objects.nonNull(productPrice.getProduct().getVendor())
                        ? Vendor.builder()
                        .id(productPrice.getProduct().getVendor().getVendorId())
                        .name(Objects.nonNull(productPrice.getProduct().getVendor().getName()) ? productPrice.getProduct().getVendor().getName() : null)
                        .commission(Objects.nonNull(productPrice.getProduct().getVendor().getCommission()) ? productPrice.getProduct().getVendor().getCommission() : null)
                        .vat(Objects.nonNull(productPrice.getProduct().getVendor().getVat()) ? productPrice.getProduct().getVendor().getVat() : null).build() : null)
                .preOffer(Objects.nonNull(offer.getPreOffer()) ? offer.getPreOffer() : null)
                .offer(offer)
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
                .defaultPaymentMethod(productPrice.getDefaultPaymentMethod())
                .paymentMethod(productPrice.getPaymentMethod())
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

    private BockingProduct getBockingProductUpdate(final BockingProduct productPrice, final Offer offer) {
        return bockingProductMapper.entityToModel(bockingProductRepository.save(bockingProductMapper.modelToEntity(BockingProduct
                .builder()
                .id(productPrice.getId())
                .productName(Objects.nonNull(productPrice.getProduct())?productPrice.getProduct().getName():null)
                .vendor(Objects.nonNull(productPrice.getProduct())
                        ? Vendor.builder().id(productPrice.getProduct().getVendor().getVendorId())
                        .name(productPrice.getProduct().getVendor().getName())
                        .vat(productPrice.getProduct().getVendor().getVat())
                        .commission(productPrice.getProduct().getVendor().getCommission()).build() : null)
                .offer(offer)
                .productId(Objects.nonNull(productPrice.getProduct())? productPrice.getProduct().getId():null)
                .paymentDetails(productPrice.getPaymentDetails())
                .marketing(BockingProductHelper.getMarketing(productPrice))//ACA
                .vatPercentage(Objects.nonNull(productPrice) ? BockingProductHelper.getVat(productPrice):null)
                .vatValue(BockingProductHelper.getVatValue(productPrice))
                .paymentReference(productPrice.getPaymentReference())
                .availabilityTo(BockingProductHelper.getAvailabilityTo(productPrice))
                .defaultAvailabilityTo(productPrice.getDefaultAvailabilityTo())
                .defaultDescription(productPrice.getDefaultDescription())
                .defaultPaymentDetails(productPrice.getDefaultPaymentDetails())
                .description(BockingProductHelper.getDescription(productPrice))
                .commissionPercentage(BockingProductHelper.getCommission(productPrice))
                .commissionValue(BockingProductHelper.getCommissionValue(productPrice))
                .defaultVat(productPrice.getDefaultVat())
                .basePrice(BockingProductHelper.getBasePrice(productPrice))
                .totalWithCommission(BockingProductHelper.getResultTotalWithCommission(productPrice,productPrice.getProduct()))
                .totalWithVat(BockingProductHelper.getResultTotalWithVat(productPrice))
                .defaultMarketing(productPrice.getDefaultMarketing())
                .defaultCommission(productPrice.getDefaultCommission())
                .defaultBasePrice(productPrice.getDefaultBasePrice())
                .showPrice(productPrice.getShowPrice())
                .requiresPayment(productPrice.getRequiresPayment())
                .availabilityFrom(BockingProductHelper.getAvailabilityFrom(productPrice))
                .defaultAvailabilityFrom(productPrice.getDefaultAvailabilityFrom())
                .defaultAvailabilityTo(productPrice.getDefaultAvailabilityTo())
                .product(Objects.nonNull(productPrice.getProduct()) ? Product.builder().id(productPrice.getProduct().getId()).build() : null)
                .defaultPaymentMethod(Objects.nonNull(productPrice.getDefaultPaymentMethod()) ? productPrice.getDefaultPaymentMethod() : null)
                .paymentMethod(Objects.nonNull(productPrice.getPaymentMethod()) ? productPrice.getPaymentMethod() : null)
                .files(Objects.nonNull(productPrice.getId()) ? fileApiMapper
                        .modelsToResponseList(fileService
                                .getFileProductPreOffer(productPrice.getId())) : null)
                .build())));
    }

    private void assignedBuildOfferResult(Offer offer, List<Offer> OffersResult,
                                          List<BockingProduct> productPrices) {
        OffersResult.add(Offer
                .builder()
                .preOffer(Objects.nonNull(offer.getPreOffer()) ? offer.getPreOffer() : null)
                .currency(offer.getCurrency())
                .pdfUrl(Objects.nonNull(offer.getPdfUrl()) ? offer.getPdfUrl() : null)
                .name(offer.getName())
                .number(offer.getNumber())
                .id(offer.getId())
                .products(productPrices)
                .description(offer.getDescription())
                .customer(Objects.nonNull(offer.getCustomer()) ? offer.getCustomer() : null)
                .discount(offer.getDiscount())
                .total(getTotal(offer))
                .subtotal(getTotal(offer))
                .textToClient(offer.getTextToClient())
                .defaultExpiration(offer.getDefaultExpiration())
                .paymentRequired(offer.getPaymentRequired())
                .expirationTime(offer.getExpirationTime())
                .globalStatus(offer.getGlobalStatus())
                .version(offer.getVersion())
                .status(offer.getStatus())
                .restricted(offer.getRestricted())
                .creationTime(offer.getCreationTime())
                .creationUser(offer.getCreationUser())
                .modificationTime(offer.getModificationTime())
                .modificationUser(offer.getModificationUser())
                .build());
    }

    private BigDecimal getTotal(final Offer offer) {
        return plusProductTotal(offer) == null ? BigDecimal.ZERO :
                plusProductTotal(offer);
    }

    private static BigDecimal plusProductTotal(final Offer offer) {
        return !CollectionUtils.isEmpty(offer.getProducts()) ? offer.getProducts().stream()
                .map(bockingProduct -> {
                    if (bockingProduct.getRequiresPayment() == Boolean.TRUE &&
                            bockingProduct.getShowPrice()==Boolean.TRUE)  {
                        return bockingProduct.getTotalWithVat();
                    } else {
                        return BigDecimal.ZERO; // Si RequiresPayment es false, devuelve 0.
                    }
                })
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add) : BigDecimal.ZERO;
    }

    private BigDecimal getSubtotal(final Offer offer) {
        return plusProductPrices(offer) == null ? BigDecimal.ZERO : plusProductPrices(offer);
    }

    private BigDecimal plusProductPrices(final Offer offer) {

        AtomicReference<BigDecimal> result = new AtomicReference<>();
        return !CollectionUtils.isEmpty(offer.getProducts()) ? offer.getProducts().stream()
                .map(bockingProduct -> {
                    if (Boolean.TRUE.equals(bockingProduct.getDefaultBasePrice())) {
                        Product product = productService.findById(bockingProduct.getProductId());
                        return Objects.nonNull(product) ? product.getBasePrice() : null;
                    } else {
                        return bockingProduct.getBasePrice();
                    }
                })
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add) : null;
    }

    private Offer assignedBuildOffer(Offer offer,
                                     List<BockingProduct> productPrices) {
        return Offer
                .builder()
                .files(!CollectionUtils.isEmpty(offer.getFiles()) ? offer.getFiles() : null)
                .customer(Objects.nonNull(offer.getCustomer()) ? offer.getCustomer() : null)
                .currency(offer.getCurrency())
                .name(offer.getName())
                .number(offer.getNumber())
                .id(offer.getId())
                .products(productPrices)
                .description(offer.getDescription())
                .discount(offer.getDiscount())
                .textToClient(offer.getTextToClient())
                .defaultExpiration(offer.getDefaultExpiration())
                .paymentRequired(offer.getPaymentRequired())
                .subtotal(getTotal(offer))
                .total(getTotal(offer))
                .expirationTime(offer.getExpirationTime())
                .globalStatus(offer.getGlobalStatus())
                .pdfUrl(offer.getPdfUrl())
                .version(offer.getVersion())
                .status(offer.getStatus())
                .preOffer(Objects.nonNull(offer.getPreOffer()) ? offer.getPreOffer() : null)
                .restricted(offer.getRestricted())
                .creationTime(offer.getCreationTime())
                .creationUser(offer.getCreationUser())
                .modificationTime(offer.getModificationTime())
                .modificationUser(offer.getModificationUser())
                .build();
    }

    private BigDecimal getSubtotal(final PreOffer preOffer) {
       /* if (Objects.nonNull(preOffer.getSubtotal())) {
            return preOffer.getSubtotal();
        }*/
        return plusProductPrices(preOffer) == null ? BigDecimal.ZERO : plusProductPrices(preOffer);
    }

    private BigDecimal plusProductPrices(final PreOffer preOffer) {
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

    private static Specification<OfferEntity> getSpecByLastVersion(final OfferSpecification specification) {
        return (root, query, criteriaBuilder) -> {
            final Subquery<Integer> subQuery = query.subquery(Integer.class);
            final Root<OfferEntity> subRoot = subQuery.from(OfferEntity.class);
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

    @Transactional
    public Offer findById(final Integer id) {
        final Specification<OfferEntity> specificationById = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.<Integer>get("id"), id
                );
        final List<OfferEntity> findAll = repository.findAll(specificationById);
        if (CollectionUtils.isEmpty(findAll)) {
            throw new OfferNotFoundException(id);
        }
        return assignedBuildOffer(mapper.entityToModel(findAll.get(0)),
                getProductPriceList(mapper.entityToModel(findAll.get(0))));
    }

    public void deleteById(final Integer id) {
        existsById(id);
        repository.deleteById(id);
    }

    public void deleteStatus(final Integer id) {
        existsById(id);

        repository.deleteById(id);
    }

    public void existsById(final Integer id) {
        if (!repository.existsById(id)) {
            throw new OfferNotFoundException(id);
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

    public Long count(final OfferSpecification specification) {
        final Specification<OfferEntity> byLastVersion = getSpecByLastVersion(specification);
        return repository.count(byLastVersion);
    }

    public List<Offer> findByPreOffer(final PreOffer preOffer) {
        log.info("FindByPreOffer consulting PreOffer Id : {}",preOffer.getId());
        return mapper.entitiesToModelList(repository.findByPreOfferId(preOffer.getId()));
    }
}
