package com.martzatech.vdhg.crmprojectback.application.services;

import com.martzatech.vdhg.crmprojectback.application.enums.DeletedStatusEnum;
import com.martzatech.vdhg.crmprojectback.application.enums.DiscountTypeEnum;
import com.martzatech.vdhg.crmprojectback.application.exceptions.BusinessRuleException;
import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.BockingProductNotFoundException;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.File;
import com.martzatech.vdhg.crmprojectback.domains.chat.services.FileService;
import com.martzatech.vdhg.crmprojectback.domains.customers.enums.OfferStatusEnum;
import com.martzatech.vdhg.crmprojectback.domains.customers.enums.PreOfferStatusEnum;
import com.martzatech.vdhg.crmprojectback.domains.customers.mappers.BockingProductMapper;
import com.martzatech.vdhg.crmprojectback.domains.customers.mappers.PreOfferMapper;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.*;
import com.martzatech.vdhg.crmprojectback.domains.customers.repositories.BockingProductRepository;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.OfferService;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.PreOfferService;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.ProductService;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.PreOfferSpecification;
import com.martzatech.vdhg.crmprojectback.domains.security.models.DeletedStatus;
import com.martzatech.vdhg.crmprojectback.domains.vendors.services.VendorService;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.FileApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.FileResponse;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants.AZURE_PATH;
import static com.martzatech.vdhg.crmprojectback.application.helper.FileHelper.validateExtensionFile;

@AllArgsConstructor
@Slf4j
@Service
public class PreOfferManagementService {

    public static final int GLOBAL_STATUS_CLOSE = 2;
    public static final int GLOBAL_STATUS_IN_PROGRESS = 1;
    public static final String CANNOT_GENERATE_PDF_PRE_OFFER_ISN_T_OPEN_OR_IS_EXPIRED = "Cannot generate pdf. PreOffer isn't open or is expired";
    private final FileService fileService;
    private final AzureFileService azureFileService;
    private PreOfferService preOfferService;
    private OfferService offerService;
    private ProductService productService;
    private SecurityManagementService securityManagementService;
    private ReportPdfService reportService;
    private BockingProductMapper priceMapper;
    private VendorService vendorService;
    private BockingProductRepository priceRepository;
    private FileApiMapper fileApiMapper;
    private final PreOfferMapper preOfferMapper;
    public static String extractFileNameWithoutExtension(String fileName) {
        // Buscar el último punto (.) en la cadena
        int lastDotIndex = fileName.lastIndexOf('.');

        // Verificar si se encontró un punto
        if (lastDotIndex >= 0) {
            // Extraer el nombre del archivo sin la extensión
            return fileName.substring(0, lastDotIndex);
        } else {
            // Si no se encontró un punto, devolver la cadena original
            return fileName;
        }
    }

    private static void validateSubTotal(final PreOffer preOffer) {
        if (Objects.nonNull(preOffer.getSubtotal())
                && preOffer.getSubtotal().compareTo(BigDecimal.ZERO) > 0
                && preOffer.getProducts().stream().anyMatch(pp -> Objects.nonNull(pp.getProduct().getBasePrice())
                && pp.getProduct().getBasePrice().compareTo(BigDecimal.ZERO) > 0)
                && preOffer.getSubtotal().compareTo(plusProductPrices(preOffer)) != 0) {
            throw new BusinessRuleException("Can only set subtotal or prices by products. Not both.");
        }
      /*  if (Boolean.TRUE.equals(preOffer.getPaymentRequired())
                && (
                Objects.isNull(preOffer.getSubtotal())
                        || preOffer.getSubtotal().compareTo(BigDecimal.ZERO) < 1)
                && preOffer.getProducts().stream()
                .noneMatch(pp -> Objects.nonNull(pp.getPrice()) && pp.getPrice().compareTo(BigDecimal.ZERO) > 0)) {
            throw new BusinessRuleException("Subtotal or prices by products is required.");
        }*/
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


    public void deleteProductPreOffer(final Integer productId, final Integer preOfferId) {
        validId(preOfferId);
        validateProductBocking(productId);
        PreOffer preOffer = preOfferService.findById(preOfferId);
        if (!CollectionUtils.isEmpty(preOffer.getProducts()) &&
                preOffer.getProducts().stream().anyMatch(productPrice ->
                        productPrice.getId().intValue() == productId)) {
            preOffer.getProducts().forEach(productPrice -> priceRepository.delete(productId));
        } else {
            throw new BusinessRuleException("There is no product associated with the pre-offer this id:" + productId);
        }
    }

    public PreOffer findById(final Integer id) {
        PreOffer preOffer = preOfferService.findById(id);

        return preOffer;
    }

    @Transactional
    public void deleteProductFilePreOffer(final Integer bockingProductId, final Integer preOfferId, final Integer fileId) {
        validId(preOfferId);
        validateProductBocking(bockingProductId);
        PreOffer preOffer = preOfferService.findById(preOfferId);
        validateProductAssociatePreOffer(bockingProductId, fileId, preOffer);
    }

    private void validateProductAssociatePreOffer(Integer bockingProductId, Integer fileId, PreOffer preOffer) {
        if (!CollectionUtils.isEmpty(preOffer.getProducts())) {
            for (BockingProduct bockingProduct : preOffer.getProducts()) {
                getProductBockingId(bockingProductId, fileId, bockingProduct);
            }
        }
    }

    private void getProductBockingId(Integer bockingProductId, Integer fileId, BockingProduct bockingProduct) {
        if (bockingProduct.getId().intValue() == bockingProductId) {
            if (!CollectionUtils.isEmpty(bockingProduct.getFiles())) {
                List<FileResponse> filesCopy = new ArrayList<>(bockingProduct.getFiles());
                boolean fileFound = false;

                for (FileResponse fileResponse : bockingProduct.getFiles()) {
                    if (fileResponse.getId().intValue() == fileId) {
                        filesCopy.remove(fileResponse);
                        fileFound = true;
                        break;
                    }
                }

                if (!fileFound) {
                    throw new BusinessRuleException("This file does not exist for this product or is already deleted fileId: " + fileId);
                }

                bockingProduct.setFiles(filesCopy);
                priceRepository.save(priceMapper.modelToEntity(bockingProduct));
            } else {
                throw new BusinessRuleException("It does not have a file associated with the BockingProduct");
            }
        }
    }


    @Transactional
    public PreOffer save(final PreOffer preOffer) {
        validations(preOffer);
        List<BockingProduct> products = new ArrayList<>();
        List<PreOffer> preOffers = new ArrayList<>();
        PreOffer preOfferSave = preOfferService.save(build(preOffer));
        PreOffer preOfferPersist;

        if (Objects.nonNull(preOffer.getNumber())) {
            preOffers = preOfferService.findByNumber(preOfferSave.getNumber());
            getLastVersionProductPreOffer(preOffers, preOfferSave, products);
        }

        preOfferPersist = preOfferSave ;
        if (products.size() > 0) {
            preOfferPersist.setProducts(products);
        }else{
            products = priceMapper.entitiesToModelList(priceRepository.findByPreOffer( preOfferMapper.modelToEntity(preOfferSave)));
            if(!CollectionUtils.isEmpty(products)){
                preOfferPersist.setProducts(products);
            }
        }
        return preOfferPersist;
    }



    private void getLastVersionProductPreOffer(List<PreOffer> preOffers, PreOffer preOfferSave, List<BockingProduct> products) {
        preOffers.stream()
                .filter(preOfferR -> preOfferR.getVersion().intValue() == (preOfferSave.getVersion() - 1))
                .forEach(preOfferR -> {
                    if (!CollectionUtils.isEmpty(preOfferR.getProducts())) {
                        preOfferR.getProducts().forEach(bockingProduct -> {
                            bockingProduct.setId(null);
                            bockingProduct.setPreOffer(preOfferSave);
                            List<FileResponse> files =bockingProduct.getFiles();
                            bockingProduct.setFiles(null);
                            BockingProduct productSave = priceMapper.entityToModel(priceRepository
                                    .save(priceMapper.modelToEntity(bockingProduct)));
                            products.add(productSave);

                            if(!CollectionUtils.isEmpty(files)){
                                files.stream().forEach(fileResponse -> {
                                    fileService.save(
                                            File.builder()
                                                    .url(fileResponse.getUrl())
                                                    .extension(fileResponse.getExtension())
                                                    .name(fileResponse.getName())
                                                    .bockingProduct(List.of(productSave))
                                                    .status(DeletedStatus.builder().id(DeletedStatusEnum.ACTIVE.getId()).build())
                                                    .creationUser(securityManagementService.findCurrentUser())
                                                    .creationTime(LocalDateTime.now())
                                                    .build()
                                    );
                                });
                            }

                        });
                    }
                });
    }


    public File addFile(final Integer id, final Integer productId, final MultipartFile file, final String extension) {
        String extensionFile = FilenameUtils.getExtension(file.getOriginalFilename());
        validateExtensionFile(extension, extensionFile);
        validId(id);
        PreOffer preOffer = preOfferService.findById(id);
        File messageFile = null;
        List<BockingProduct> products = new ArrayList<>();
        if (CollectionUtils.isEmpty(preOffer.getProducts())) {
            //preOffer.getProducts().forEach(productPrice ->products.add(productPrice.getProduct()));
            throw new BusinessRuleException("This preOffer does not have a product associated with it");
        } else {
            if (preOffer.getProducts().stream().anyMatch(productPrice -> productPrice.getId().intValue() == productId)) {
                preOffer.getProducts().stream().filter(bockingProduct -> bockingProduct.getId().intValue() == productId)
                        .forEach(productPrice -> products.add(productPrice));
                if (Objects.nonNull(file)) {
                    String urlFile = azureFileService.uploadFile(file, AZURE_PATH, extension);
                    messageFile = fileService.save(
                            File.builder()
                                    .url(urlFile)
                                    .extension(extension)
                                    .name(extractFileNameWithoutExtension(file.getOriginalFilename()))
                                    .bockingProduct(!CollectionUtils.isEmpty(products) ? products : null)
                                    .status(DeletedStatus.builder().id(DeletedStatusEnum.ACTIVE.getId()).build())
                                    .creationUser(securityManagementService.findCurrentUser())
                                    .creationTime(LocalDateTime.now())
                                    .build()
                    );
                }
            } else {
                throw new BusinessRuleException("This pre offer does not have any product associated with the id :" + productId);
            }
        }
        return messageFile;
    }

    private void validId(final Integer id) {
        if (Objects.nonNull(id)) {
            preOfferService.existsById(id);
        }
    }

    @Transactional
    public PreOffer saveProduct(final Integer preOfferId, final PreOfferProduct preOfferProduct) {
        log.info("Save Product Bocking PreOffer ID :{}", preOfferId);
        log.info("Save Product Bocking PreOffer Product Catalog ID :{}", preOfferProduct.getProductId());
        List<BockingProduct> bockingProducts = new ArrayList<>();

        PreOffer preOffer = preOfferService.findById(preOfferId);
       // validations(preOffer);
        validateProduct(preOfferProduct.getProductId());
        validateNumber(preOffer);
        validateStatusPreOffer(preOffer);

        BockingProduct product = priceMapper.entityToModel(priceRepository
                .save(priceMapper.modelToEntity(getProductPrice(preOfferProduct,
                        productService.findById(preOfferProduct.getProductId()), preOffer))));

        if (!CollectionUtils.isEmpty(preOffer.getProducts())) {
            if(preOffer.getProducts().stream().noneMatch(bockingProduct -> bockingProduct.getId().equals(product.getId()))) {
                bockingProducts.addAll(preOffer.getProducts());
                bockingProducts.add(product);
            }
        } else {
            bockingProducts.add(product);
        }

        if( Objects.nonNull(product) &&
                (Objects.requireNonNullElse(preOfferProduct.getId(), 0) != 0) ){
            bockingProducts.addAll(preOffer.getProducts());
            return  preOffer;
        }
        preOffer.setProducts(bockingProducts);
        return preOfferService.save(build(preOffer));
    }

    private static void validateStatusPreOffer(PreOffer preOffer) {
        if (!(preOffer.getGlobalStatus() == PreOfferStatusEnum.WORKING
                        && preOffer.getStatus() == OfferStatusEnum.OPEN)) {
            throw new BusinessRuleException("The products can be updated only in pre-offers with status Draft.");
        }
    }

    private BockingProduct getProductPrice(PreOfferProduct preOfferProduct, Product product, PreOffer offer) {
        BockingProduct bockingProduct = null;
        if (Objects.requireNonNullElse(preOfferProduct.getId(), 0) != 0) {
            if(!priceRepository.existsById(preOfferProduct.getId())){
                throw new BusinessRuleException("The product to update does not exist");
            }
            bockingProduct = priceMapper.entityToModel(priceRepository.findById(preOfferProduct.getId()).get());
        }else{
            log.info("Inf PreOffer Product:{}",preOfferProduct);
        }
        return BockingProduct
                .builder()
                .id(Objects.nonNull(bockingProduct) ? bockingProduct.getId() : null)
                .productId(product.getId())
                .productName(product.getName())
                .product(product)
                .preOffer(offer)
                .files(Objects.nonNull(bockingProduct)
                        && !CollectionUtils.isEmpty(bockingProduct.getFiles())
                        ? bockingProduct.getFiles():null)
                .totalWithVat(Objects.nonNull(bockingProduct) ? bockingProduct.getTotalWithVat():null)//TODO:PENDIENTE
                .requiresPayment(preOfferProduct.getRequiresPayment())
                .showPrice(preOfferProduct.getShowPrice())
                .defaultBasePrice(preOfferProduct.getDefaultBasePrice())
                .basePrice(preOfferProduct.getDefaultBasePrice() == Boolean.TRUE ? product.getBasePrice() : preOfferProduct.getBasePrice())
                .defaultVat(preOfferProduct.getDefaultVat())
                .vatPercentage(preOfferProduct.getDefaultVat() == Boolean.TRUE ? Objects.isNull(product.getProductVat()) ? null : product.getProductVat() : preOfferProduct.getVat())
                .vatValue(Objects.nonNull(bockingProduct)? bockingProduct.getVatValue():null)
                .defaultCommission(preOfferProduct.getDefaultCommission())
                .commissionPercentage(preOfferProduct.getDefaultCommission() == Boolean.TRUE ? product.getProductCommission() : preOfferProduct.getCommission())
                .commissionValue(Objects.nonNull(bockingProduct)?bockingProduct.getCommissionValue():null)
                .defaultDescription(preOfferProduct.getDefaultDescription())
                .description(preOfferProduct.getDefaultDescription() == Boolean.TRUE ? product.getDescription() : preOfferProduct.getDescription())
                .defaultMarketing(preOfferProduct.getDefaultMarketing())
                .marketing(preOfferProduct.getDefaultMarketing() == Boolean.TRUE ? product.getMarketing() : preOfferProduct.getMarketing())
                .defaultAvailabilityFrom(preOfferProduct.getDefaultAvailabilityFrom())
                .availabilityFrom(preOfferProduct.getDefaultAvailabilityFrom() == Boolean.TRUE ? product.getAvailabilityFrom() : preOfferProduct.getAvailabilityFrom())
                .defaultAvailabilityTo(preOfferProduct.getDefaultAvailabilityTo())
                .availabilityTo(preOfferProduct.getDefaultAvailabilityTo() == Boolean.TRUE ? product.getAvailabilityTo() : preOfferProduct.getAvailabilityTo())
                .paymentReference(preOfferProduct.getPaymentReference())
                .defaultPaymentDetails(preOfferProduct.getDefaultPaymentDetails())
                .paymentDetails(preOfferProduct.getDefaultPaymentDetails() == Boolean.TRUE ? Objects.isNull(product.getVendor()) ? null : vendorService.findById(product.getVendor().getVendorId()).getPaymentDetails() : preOfferProduct.getPaymentDetails())//TODO:verificar esto
                .build();
    }

    private void validateProduct(Integer productId) {
        productService.existsById(productId);
    }

    private void validateProductBocking(Integer bockingProductId) {
        if (!priceRepository.existsById(bockingProductId)) {
            throw new BockingProductNotFoundException(bockingProductId);
        }
    }

    private PreOffer build(final PreOffer preOffer) {
        final PreOffer fromDDBB = Objects.nonNull(preOffer.getId())
                ? checkStatusAndGetById(preOffer)
                .withName(preOffer.getName())
                .withDescription(preOffer.getDescription())
                .withProducts(!CollectionUtils.isEmpty(preOffer.getProducts()) ? preOffer.getProducts():null)
                .withCurrency(preOffer.getCurrency())
                .withDefaultExpiration(preOffer.getDefaultExpiration())
                .withTextToClient(preOffer.getTextToClient())
                .withDiscount(preOffer.getDiscount())
                .withPaymentRequired(preOffer.getPaymentRequired())
                .withExpirationTime(getExpirationTime(preOffer))
                .withModificationUser(securityManagementService.findCurrentUser())
                .withPaymentRequired(Objects.nonNull(preOffer.getPaymentRequired())?preOffer.getPaymentRequired():false)
                : preOfferService.save(buildNumber(buildVersion(preOffer)));
        return fromDDBB
                .withStatus(
                        Objects.isNull(preOffer.getId())
                                ? OfferStatusEnum.OPEN
                                : fromDDBB.getStatus()
                )
                .withGlobalStatus(PreOfferStatusEnum.WORKING)
                .withDefaultExpiration(fromDDBB.getDefaultExpiration())
                .withExpirationTime(getExpirationTime(fromDDBB))
                .withProducts(fromDDBB.getProducts())
                .withSubtotal(getTotal(fromDDBB))
                .withTotal(getTotal(fromDDBB))
                .withPaymentRequired(Objects.nonNull(fromDDBB.getPaymentRequired())
                        ?fromDDBB.getPaymentRequired():false)
                .withCreationTime(
                        Objects.isNull(preOffer.getId())
                                ? LocalDateTime.now()
                                : fromDDBB.getCreationTime()
                )
                .withTextToClient(Objects.nonNull(fromDDBB
                        .getTextToClient()) ? fromDDBB.getTextToClient() : null)

                .withModificationTime(
                        Objects.nonNull(preOffer.getId())
                                ? LocalDateTime.now()
                                : null
                )
                .withCreationUser(
                        Objects.isNull(preOffer.getId())
                                ? securityManagementService.findCurrentUser()
                                : fromDDBB.getCreationUser()
                )
                .withModificationUser(
                        Objects.nonNull(preOffer.getId())
                                ? fromDDBB.getModificationUser()
                                : null
                );
    }

    private PreOffer checkStatusAndGetById(final PreOffer preOffer) {
        final PreOffer byId = preOfferService.findById(preOffer.getId());

        if (byId.getGlobalStatus() != PreOfferStatusEnum.WORKING) {
            throw new BusinessRuleException("Only can be updated opened pre offers");
        }

        return byId;
    }

    private PreOffer buildNumber(final PreOffer preOffer) {
        if (Objects.isNull(preOffer.getId()) && Objects.isNull(preOffer.getNumber())) {
            return preOffer.withNumber(preOfferService.getCurrentNumber());
        }
        return preOffer;
    }

    private PreOffer buildVersion(final PreOffer preOffer) {
        if (Objects.isNull(preOffer.getId())) {
            if (Objects.isNull(preOffer.getNumber())) {
                return preOffer.withVersion(1);
            } else {
                return preOffer.withVersion(preOfferService.getCurrentVersion(preOffer.getNumber()));
            }
        }
        return preOffer;
    }

    private void validations(final PreOffer preOffer) {
        validateNumber(preOffer);
        //validateProducts(preOffer);
        //validateSubTotal(preOffer);
        //validateDiscount(preOffer);

        validateStatus(preOffer);

    }

    private void validateNamePreOffer(PreOffer preOffer) {
        if (StringUtils.isNotBlank(preOffer.getName())) {
            if (Objects.nonNull(preOfferService.findByName(preOffer.getName()))) {
                throw new BusinessRuleException("preOffers name  exists");
            }
        }
    }

    private void validateNumber(final PreOffer preOffer) {
        if (Objects.isNull(preOffer.getId()) && Objects.nonNull(preOffer.getNumber())) {
            if (!preOfferService.existsByNumber(preOffer.getNumber())) {
                throw new BusinessRuleException("PreOffer Number not exists");
            }
        }
    }

    private void validateDiscount(final PreOffer preOffer) {
        if (Objects.nonNull(preOffer.getDiscount())) {
            if (Objects.isNull(preOffer.getDiscount().getValue())) {
                throw new BusinessRuleException("When set discount, value is mandatory");
            }
            if (Objects.isNull(preOffer.getDiscount().getType())) {
                throw new BusinessRuleException("When set discount, type is mandatory");
            }
            if (DiscountTypeEnum.PERCENTAGE.name().equals(preOffer.getDiscount().getType())
                    && (preOffer.getDiscount().getValue().intValue() < 1
                    || preOffer.getDiscount().getValue().intValue() > 100)) {
                throw new BusinessRuleException("Discount value should be between 1 and 100");
            }
            if (DiscountTypeEnum.FIXED.name().equals(preOffer.getDiscount().getType())
                    && preOffer.getDiscount().getValue().compareTo(getSubtotal(preOffer)) > 0) {
                throw new BusinessRuleException("Discount value cannot greater than subTotal value");
            }
        }
    }

    private void validateProducts(final PreOffer preOffer) {
        if (CollectionUtils.isEmpty(preOffer.getProducts())) {
            throw new BusinessRuleException("Products List is mandatory");
        }
        if (preOffer.getProducts().stream()
                .map(BockingProduct::getProduct).map(Product::getId).anyMatch(Objects::isNull)) {
            throw new BusinessRuleException("When set product, its id is required");
        }
        preOffer.getProducts().forEach(productPrice -> productService.existsById(productPrice.getProduct().getId()));
    }

    private void validateStatus(final PreOffer preOffer) {
        if (Objects.nonNull(preOffer.getNumber())) {
            final List<PreOffer> preOffers = preOfferService.findByNumber(preOffer.getNumber());
            if (preOffers.stream().anyMatch(o -> o.getGlobalStatus() == PreOfferStatusEnum.CLOSED)) {
                throw new BusinessRuleException("Cannot create a new version. Current number is closed.");
            }

            if (preOffers.stream()
                    .anyMatch(o -> o.getGlobalStatus() == PreOfferStatusEnum.WORKING
                            && o.getStatus() == OfferStatusEnum.OPEN)) {
                throw new BusinessRuleException("The current version status is Open." +
                        " Please keep editing the current version or generate a PDF before creating a new version.");
            }
        }
    }

    private BigDecimal getSubtotal(final PreOffer preOffer) {
       /* if (Objects.nonNull(preOffer.getSubtotal())) {
            return preOffer.getSubtotal();
        }*/
        return plusProductPrices(preOffer) == null ? BigDecimal.ZERO : plusProductPrices(preOffer);
    }

    private BigDecimal getTotal(final PreOffer preOffer) {
        /*final BigDecimal subtotal = getSubtotal(preOffer);
        return subtotal.subtract(
                Objects.nonNull(preOffer.getDiscount())
                        ? DiscountTypeEnum.FIXED.name().equalsIgnoreCase(preOffer.getDiscount().getType())
                        ? preOffer.getDiscount().getValue()
                        : subtotal.multiply(preOffer.getDiscount().getValue().divide(new BigDecimal(100)))
                        : BigDecimal.ZERO
        ).setScale(2, RoundingMode.HALF_EVEN);*/
        return plusProductTotal(preOffer) == null ? BigDecimal.ZERO : plusProductTotal(preOffer);
    }

    private static BigDecimal plusProductTotal(final PreOffer preOffer) {
        return !CollectionUtils.isEmpty(preOffer.getProducts()) ? preOffer.getProducts().stream()
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

    @Transactional
    public List<PreOffer> findAll(final PreOfferSpecification specification,
                                  final Integer page, final Integer size, final String direction, final String attribute) {
        final Direction directionEnum =
                Arrays.stream(Direction.values()).anyMatch(v -> v.name().equalsIgnoreCase(direction))
                        ? Direction.fromString(direction)
                        : Direction.DESC;
        final Sort sort = Sort.by(directionEnum, attribute);
        final Pageable pageable = PageRequest.of(page, size, sort);
        return preOfferService.findAll(specification, pageable);
    }
    @Transactional
    public void deleteById(final Integer id) {
        log.info("Delete PreOffer id :{}",id);

        final PreOffer preOffer = preOfferService.findById(id);
        checkIfHasAssociatedOffer(preOffer);
        preOfferService.deleteById(preOffer.getId());
    }
    @Transactional
    public void deleteByNumber(final Integer number) {
        log.info("DeleteByNumber PreOffer for number : {}",number);
        final List<PreOffer> preOffers = preOfferService.findByNumber(number);

        if (CollectionUtils.isEmpty(preOffers)) {
            throw new BusinessRuleException("There are no preOffers for this number " + number);
        }

        preOffers.forEach(this::checkIfHasAssociatedOffer);

        preOffers.forEach(offer -> preOfferService.deleteById(offer.getId()));
    }

    private void checkIfHasAssociatedOffer(final PreOffer preOffer) {
        log.info("Check If Has Associated Offer, PreOffer id:{}",preOffer.getId());
        final List<Offer> byPreOffer = offerService.findByPreOffer(preOffer);
        if (!CollectionUtils.isEmpty(byPreOffer)) {
            final String message = String
                    .format(
                            "The PreOffer id %s, number %s, version %s cannot be deleted. "
                                    + "It is related to %s offers",
                            preOffer.getId(), preOffer.getNumber(), preOffer.getVersion(),
                            byPreOffer.size()
                    );
            log.error(message);
            throw new BusinessRuleException(message);
        }
    }

    public Map<String, String> getPdfById(final Integer id) throws IOException {
        log.info("Generate Pdf PreOffer ID: {}",id);
        final PreOffer preOffer = preOfferService.findById(id);

        if (!(preOffer.getStatus() == OfferStatusEnum.OPEN && preOffer.getGlobalStatus() == PreOfferStatusEnum.WORKING
                && (Objects.isNull(preOffer.getExpirationTime()) || preOffer.getExpirationTime().isAfter(LocalDateTime.now())))) {
            throw new BusinessRuleException(CANNOT_GENERATE_PDF_PRE_OFFER_ISN_T_OPEN_OR_IS_EXPIRED);
        }


        final String pdfUrl = reportService.exportPreOfferPdf(preOffer);
        fileService
                .save(File.builder()
                        .text("")
                        .name("P - " + preOffer.getName())
                        .url(pdfUrl)
                        .extension("pdf")
                        .creationTime(LocalDateTime.now())
                        .creationUser(securityManagementService.findCurrentUser())
                        .build());
        changeStatus(preOffer.withPdfUrl(pdfUrl), OfferStatusEnum.CONFIRMED);//TODO:status 1 inProgress
        return Map.of("url", pdfUrl);
    }

    @Transactional
    public void closeById(final Integer id) {
        final PreOffer preOffer = preOfferService.findById(id);

        preOfferService.findByNumber(preOffer.getNumber())
                .forEach(o -> changeStatus(o, OfferStatusEnum.CLOSED));//TODO:[Element1, Element2, Element 3]
    }

    private void changeStatus(final PreOffer preOffer, final OfferStatusEnum status) {
        log.info("changing status of the preOffer with the id:{}", preOffer.getId());



        preOfferService.save(preOffer
                .withStatus(status.equals(OfferStatusEnum.CONFIRMED) ? OfferStatusEnum.CONFIRMED : preOffer.getStatus())
                .withGlobalStatus(status.getId() == OfferStatusEnum.CLOSED.getId() ? PreOfferStatusEnum.CLOSED : PreOfferStatusEnum.WORKING)
                .withModificationUser(securityManagementService.findCurrentUser())
                .withModificationTime(LocalDateTime.now()));
    }
}
