package com.martzatech.vdhg.crmprojectback.application.services;

import com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants;
import com.martzatech.vdhg.crmprojectback.application.enums.DeletedStatusEnum;
import com.martzatech.vdhg.crmprojectback.application.enums.DiscountTypeEnum;
import com.martzatech.vdhg.crmprojectback.application.exceptions.BusinessRuleException;
import com.martzatech.vdhg.crmprojectback.application.exceptions.FieldIsMissingException;
import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.BockingProductNotFoundException;
import com.martzatech.vdhg.crmprojectback.domains.chat.enums.ChatMessageTypeEnum;
import com.martzatech.vdhg.crmprojectback.domains.chat.mappers.FileMapper;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.ChatMessage;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.ChatRoom;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.File;
import com.martzatech.vdhg.crmprojectback.domains.chat.services.ChatMessageService;
import com.martzatech.vdhg.crmprojectback.domains.chat.services.ChatRoomService;
import com.martzatech.vdhg.crmprojectback.domains.chat.services.FileService;
import com.martzatech.vdhg.crmprojectback.domains.customers.enums.OfferGLobalStatusEnum;
import com.martzatech.vdhg.crmprojectback.domains.customers.enums.OfferStatusEnum;
import com.martzatech.vdhg.crmprojectback.domains.customers.mappers.BockingProductMapper;
import com.martzatech.vdhg.crmprojectback.domains.customers.mappers.OfferMapper;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.*;
import com.martzatech.vdhg.crmprojectback.domains.customers.repositories.BockingProductRepository;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.*;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.OfferSpecification;
import com.martzatech.vdhg.crmprojectback.domains.security.models.DeletedStatus;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import com.martzatech.vdhg.crmprojectback.domains.security.services.UserService;
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
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants.AZURE_PATH;
import static com.martzatech.vdhg.crmprojectback.application.helper.FileHelper.validateExtensionFile;

@AllArgsConstructor
@Slf4j
@Service
public class OfferManagementService {

    public static final int ID_USER_SYSTEM = 2;
    private final FileService fileService;
    private final AzureFileService azureFileService;
    private final OfferMapper offerMapper;
    private OfferService offerService;
    private OrderService orderService;
    private ProductService productService;
    private CustomerService customerService;
    private PreOfferService preOfferService;
    private SecurityManagementService securityManagementService;
    private ReportPdfService reportService;
    private BockingProductMapper priceMapper;
    private VendorService vendorService;
    private BockingProductRepository priceRepository;
    private ChatRoomService roomService;
    private UserService userService;
    private ChatMessageService messageService;
    private FileApiMapper fileApiMapper;
    private FileMapper fileMapper;
    private DecimalFormat decimalFormat;

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

    private static BockingProduct getBockingProduct(BockingProduct bockingProduct, Offer offer) {
        return BockingProduct
                .builder()
                .id(bockingProduct.getId())
                .defaultCommission(bockingProduct.getDefaultCommission())
                .productId(bockingProduct.getProductId())
                .preOffer(bockingProduct.getPreOffer())
                .paymentMethod(bockingProduct.getPaymentMethod())
                .defaultPaymentMethod(bockingProduct.getDefaultPaymentMethod())
                .defaultVat(bockingProduct.getDefaultVat())
                .vatPercentage(Objects.nonNull(bockingProduct.getVatPercentage()) ? bockingProduct.getVatPercentage() : null)
                .commissionPercentage(Objects.nonNull(bockingProduct.getCommissionPercentage()) ? bockingProduct.getCommissionPercentage() : null)
                .marketing(Objects.nonNull(bockingProduct.getMarketing()) ? bockingProduct.getMarketing() : null)
                .preOffer(Objects.nonNull(bockingProduct.getPreOffer()) ? bockingProduct.getPreOffer() : null)
                .defaultAvailabilityFrom(Objects.nonNull(bockingProduct.getDefaultAvailabilityFrom())
                        ? bockingProduct.getDefaultAvailabilityFrom() : null)
                .availabilityFrom(Objects.nonNull(bockingProduct.getAvailabilityFrom())
                        ? bockingProduct.getAvailabilityFrom() : null)
                .availabilityTo(Objects.nonNull(bockingProduct.getAvailabilityTo())
                        ? bockingProduct.getAvailabilityTo() : null)
                .defaultAvailabilityTo(Objects.nonNull(bockingProduct.getDefaultAvailabilityTo())
                        ? bockingProduct.getDefaultAvailabilityTo() : null)
                .defaultMarketing(Objects.nonNull(bockingProduct.getDefaultMarketing())
                        ? bockingProduct.getDefaultMarketing() : null)
                .defaultCommission(Objects.nonNull(bockingProduct.getDefaultCommission())
                        ? bockingProduct.getDefaultCommission() : null)
                .defaultDescription(Objects.nonNull(bockingProduct.getDefaultDescription())
                        ? bockingProduct.getDefaultDescription() : null)
                .showPrice(bockingProduct.getShowPrice())
                .basePrice(bockingProduct.getBasePrice())
                .defaultBasePrice(bockingProduct.getDefaultBasePrice())
                .defaultDescription(bockingProduct.getDefaultDescription())
                .description(bockingProduct.getDescription())
                .requiresPayment(bockingProduct.getRequiresPayment())
                .paymentReference(bockingProduct.getPaymentReference())
                .defaultPaymentDetails(bockingProduct.getDefaultPaymentDetails())
                .offer(offer)
                .product(Objects.nonNull(bockingProduct.getProductId()) ? Product
                        .builder().id(bockingProduct.getProductId()).build() : null)
                .files(!CollectionUtils.isEmpty(bockingProduct.getFiles()) ? bockingProduct.getFiles() : null)
                .build();
    }

    private static void validateFileFoundBockingProduct(Integer fileId, boolean fileFound) {
        if (!fileFound) {
            throw new BusinessRuleException("This file does not exist for this product" +
                    " or is already deleted fileId: " + fileId);
        }
    }

   /* private void validateSubTotal(final Offer offer) {
        if (Objects.nonNull(offer.getSubtotal())
                && offer.getSubtotal().compareTo(BigDecimal.ZERO) > 0
                && offer.getProducts().stream().anyMatch(pp -> Objects.nonNull(pp.getPrice())
                && pp.getPrice().compareTo(BigDecimal.ZERO) > 0)
                && offer.getSubtotal().compareTo(plusProductPrices(offer)) != 0) {
            throw new BusinessRuleException("Can only set subtotal or prices by products. Not both.");
        }
        if (Boolean.TRUE.equals(offer.getPaymentRequired())
                && (
                Objects.isNull(offer.getSubtotal())
                        || offer.getSubtotal().compareTo(BigDecimal.ZERO) < 1)
                && offer.getProducts().stream()
                .noneMatch(pp -> Objects.nonNull(pp.getPrice()) && pp.getPrice().compareTo(BigDecimal.ZERO) > 0)) {
            throw new BusinessRuleException("Subtotal or prices by products is required.");
        }
    }*/

    @Transactional
    public Offer save(final Offer Offer) {
        validations(Offer);
        Offer offerPersist;
        List<Offer> offers;
        Offer offerSave = offerService.save(build(Offer)
                .withDeletedStatus(DeletedStatus.builder()
                        .id(DeletedStatusEnum.ACTIVE.getId())
                        .name("Active").build()));
        List<BockingProduct> products = new ArrayList<>();

        if (Objects.nonNull(Offer.getNumber())) {
            offers = offerService.findByNumber(offerSave.getNumber());
            getVersionLastProductsOffer(offers, offerSave, products);
        }

        getProductsPreOfferAssociateOffer(Offer, offerSave, products);

        offerPersist = offerSave;
        if (products.size() > 0) {
            offerPersist.setProducts(products);
        } else {
            products = priceMapper.entitiesToModelList(priceRepository.findByOffer(offerMapper.modelToEntity(offerSave)));
            if (!CollectionUtils.isEmpty(products)) {
                offerPersist.setProducts(products);

            }
        }
        offerPersist.withSubtotal(getTotal(offerPersist));
        offerPersist.setTotal(getTotal(offerPersist));
        return offerPersist;
    }

    private void getProductsPreOfferAssociateOffer(Offer Offer, Offer offerSave, List<BockingProduct> products) {
        if (Objects.nonNull(Offer.getPreOffer())
                && Objects.nonNull(Offer.getPreOffer().getId()) && Objects.isNull(Offer.getId())) {
            PreOffer preOffer = preOfferService.findById(offerSave.getPreOffer().getId());
            if (!CollectionUtils.isEmpty(preOffer.getProducts())) {
                preOffer.getProducts().forEach(bockingProduct -> {
                    bockingProduct.setId(null);
                    bockingProduct.setOffer(offerSave);
                    bockingProduct.setPreOffer(null);
                    List<FileResponse> files = bockingProduct.getFiles();
                    bockingProduct.setFiles(null);
                    bockingProduct.setProduct(bockingProduct.getProduct());
                    BockingProduct productSave = priceMapper.entityToModel(priceRepository
                            .save(priceMapper.modelToEntity(bockingProduct)));
                    products.add(productSave);
                    getFiles(files, productSave);
                });
            }
        }
    }

    private void getVersionLastProductsOffer(List<Offer> offers, Offer offerSave, List<BockingProduct> products) {
        offers.stream()
                .filter(offer -> offer.getVersion().intValue() == (offerSave.getVersion() - 1))
                .forEach(offer -> {
                    if (!CollectionUtils.isEmpty(offer.getProducts())) {
                        offer.getProducts().forEach(bockingProduct -> {
                            bockingProduct.setId(null);
                            bockingProduct.setOffer(offerSave);
                            //bockingProduct.setPreOffer(Objects.nonNull(offerSave.getPreOffer()) ? offerSave.getPreOffer() : null);
                            List<FileResponse> files = bockingProduct.getFiles();
                            bockingProduct.setFiles(null);
                            BockingProduct productSave = priceMapper.entityToModel(priceRepository
                                    .save(priceMapper.modelToEntity(bockingProduct)));
                            products.add(productSave);
                            getFiles(files, productSave);
                        });
                    }
                });
    }

    private void getFiles(List<FileResponse> files, BockingProduct productSave) {
        if (!CollectionUtils.isEmpty(files)) {
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
    }

    public File addFile(final Integer id, final Integer productId, final MultipartFile file, final String extension) {
        String extensionFile = FilenameUtils.getExtension(file.getOriginalFilename());
        validateExtensionFile(extension, extensionFile);
        validId(id);
        Offer offer = offerService.findById(id);//CAMBIAR
        File messageFile = null;
        List<BockingProduct> products = new ArrayList<>();
        if (CollectionUtils.isEmpty(offer.getProducts())) {
            //preOffer.getProducts().forEach(productPrice ->products.add(productPrice.getProduct()));
            throw new BusinessRuleException("This Offer does not have a product associated with it");
        } else {
            if (offer.getProducts().stream().anyMatch(productPrice -> productPrice.getId().intValue() == productId)) {
                offer.getProducts().stream().filter(bockingProduct -> bockingProduct.getId().intValue() == productId)
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
                throw new BusinessRuleException("This  offer does not have any product associated with the id :" + productId);
            }
        }
        return messageFile;
    }

    private void validId(final Integer id) {
        if (Objects.nonNull(id)) {
            offerService.existsById(id);
        }
    }

    @Transactional
    public Offer saveProduct(final Integer pOfferId, final PreOfferProduct preOfferProduct) {
        log.info("Save Product Bocking Offer ID :{}", pOfferId);
        log.info("Save Product Bocking Offer Product Catalog ID :{}", preOfferProduct.getProductId());

        Offer offer = offerService.findById(pOfferId);//TODO : viene un producto associado
        List<BockingProduct> bockingProducts = new ArrayList<>();

        validateNumber(offer);

        validatePreOffer(offer);
        validateProduct(preOfferProduct.getProductId());//TODO : validamos el product catalogo
        validateBasePrice(preOfferProduct);
        validateStatusOffer(offer);

        BockingProduct product = priceMapper.entityToModel(priceRepository
                .save(priceMapper.modelToEntity(getProductPrice(preOfferProduct,
                        productService.findById(preOfferProduct.getProductId()), offer))));

        if (!CollectionUtils.isEmpty(offer.getProducts())) {
            if (offer.getProducts().stream().noneMatch(bockingProduct -> bockingProduct.getId().equals(product.getId()))) {
                bockingProducts.addAll(offer.getProducts()); // Agrega los productos existentes
                bockingProducts.add(product); // Agrega el nuevo producto
            }
        } else {
            bockingProducts.add(product);
        }

        if (Objects.nonNull(product) &&
                (Objects.requireNonNullElse(preOfferProduct.getId(), 0) != 0)) {
            bockingProducts.addAll(offer.getProducts()); // Agrega los productos existentes
            return offer;
        }

        offer.setProducts(bockingProducts);
        return offerService.save(build(offer));
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

        return null;
    }

    private static void validateStatusOffer(Offer offer) {
        if (!(offer.getGlobalStatus() == OfferGLobalStatusEnum.WORKING
                && offer.getStatus() == OfferStatusEnum.OPEN)) {
            throw new BusinessRuleException("The products can be updated only in offers with status Draft.");
        }
    }

    @Transactional
    public boolean sendEmailAndChat(final Integer offerId) {
        boolean resultBuilt = false;
        Offer offer = offerService.findById(offerId);
        validations(offer);
        Customer customer = offer.getCustomer();
        User member = userService.findByIdCustomer(customer.getId());
        if(Objects.nonNull(member)){
            ChatRoom room = validateChatRoom(member);
             resultBuilt = buildMessageCustomerChat(room, member, offer);
            if (resultBuilt) {
                offerService.save(offer
                        .withStatus(OfferStatusEnum.SENT)
                        .withGlobalStatus(OfferGLobalStatusEnum.WORKING));
            }
        }
        return resultBuilt;
    }

    private boolean buildMessageCustomerChat(ChatRoom chatRoom, User member, Offer offer) {
        if (Objects.nonNull(chatRoom)) {
            File file = fileService
                    .save(File.builder()
                            .text("")
                            .name("P - " + offer.getName())
                            .url(offer.getPdfUrl())
                            .extension("pdf")
                            .persons(Objects.nonNull(offer.getCustomer().getPerson()) ? List.of(offer.getCustomer().getPerson()) : null)
                            .creationTime(LocalDateTime.now())
                            .creationUser(securityManagementService.findCurrentUser())
                            .build());

            messageService.save(ChatMessage.builder().chatRoom(chatRoom)
                    .value(" Here is your offer " + member.getName())
                    .sender(securityManagementService.findUserById(ID_USER_SYSTEM))
                    .type(ChatMessageTypeEnum.FILE)
                    .files(List.of(fileApiMapper.modelToResponse(file)))
                    .build());
            return true;
        }
        return false;
    }

    private ChatRoom validateChatRoom(User user) {
        ChatRoom chatRoom = roomService.findByIdCustomer(user.getCustomer().getId());
        if (Objects.isNull(chatRoom)) {
            throw new BusinessRuleException("ChatRoom not exists");
        }
        return chatRoom;
    }

    private void validateBasePrice(final PreOfferProduct preOfferProduct) {
        if (Boolean.TRUE == preOfferProduct.getRequiresPayment()) {
            if (preOfferProduct.getDefaultBasePrice() == null ) {
                throw new FieldIsMissingException("Required fields DefaultBasePrice");
            }
        }
    }

    private void validateNameOffer(Offer offer) {
        if (StringUtils.isNotBlank(offer.getName())) {
            if (Objects.nonNull(offerService.findByName(offer.getName()))) {
                throw new BusinessRuleException("Offers name  exists");
            }
        }
    }

    private BockingProduct getProductPrice(PreOfferProduct preOfferProduct, Product product, Offer offer) {
        BockingProduct bockingProduct = null;
        if (Objects.requireNonNullElse(preOfferProduct.getId(), 0) != 0) {
            priceRepository.existsById(preOfferProduct.getId());
            bockingProduct = priceMapper.entityToModel(priceRepository.findById(preOfferProduct.getId()).get());
        }
        return BockingProduct
                .builder()
                .id(Objects.nonNull(bockingProduct) ? bockingProduct.getId() : null)
                .productId(product.getId())
                .productName(product.getName())
                .product(product)
                .files(Objects.nonNull(bockingProduct)
                        && !CollectionUtils.isEmpty(bockingProduct.getFiles())
                        ? bockingProduct.getFiles():null)
                .offer(Objects.nonNull(offer) ? offer : null)
                .preOffer(Objects.nonNull(offer.getPreOffer()) ? offer.getPreOffer() : null)
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
                .defaultAvailabilityTo(preOfferProduct.getDefaultAvailabilityTo())
                .defaultAvailabilityFrom(preOfferProduct.getDefaultAvailabilityFrom())
                .availabilityTo(preOfferProduct.getDefaultAvailabilityTo() == Boolean.TRUE ? product.getAvailabilityTo() : preOfferProduct.getAvailabilityTo())
                .availabilityFrom(preOfferProduct.getDefaultAvailabilityFrom() == Boolean.TRUE ? product.getAvailabilityFrom() : preOfferProduct.getAvailabilityFrom())
                .paymentReference(preOfferProduct.getPaymentReference())
                .defaultPaymentDetails(preOfferProduct.getDefaultPaymentDetails())
                .paymentDetails(preOfferProduct.getDefaultPaymentDetails() == Boolean.TRUE ? vendorService.findById(product.getVendor().getVendorId()).getPaymentDetails() : preOfferProduct.getPaymentDetails())//TODO:verificar esto
                .defaultPaymentMethod(preOfferProduct.getDefaultPaymentMethod())
                .paymentMethod(preOfferProduct.getDefaultPaymentMethod() == Boolean.TRUE ? vendorService.findById(product.getVendor().getVendorId()).getPaymentMethod() : preOfferProduct.getPaymentMethod())
                .build();
    }

    private void validateProduct(Integer productId) {
        productService.existsById(productId);
    }

    private void validateBockingProduct(Integer bockingProductId) {
        if (!priceRepository.existsById(bockingProductId)) {
            throw new BockingProductNotFoundException(bockingProductId);
        }
    }

    private Offer build(final Offer offer) {
        final Offer fromDDBB = Objects.nonNull(offer.getId())
                ? checkStatusAndGetById(offer)
                .withName(offer.getName())
                .withPreOffer(offer.getPreOffer())
                .withDescription(offer.getDescription())
                .withCustomer(offer.getCustomer())
                .withCurrency(offer.getCurrency())
                .withProducts(!CollectionUtils.isEmpty(offer.getProducts()) ? offer.getProducts() : null)
                .withDiscount(offer.getDiscount())
                .withTextToClient(offer.getTextToClient())
                .withDefaultExpiration(offer.getDefaultExpiration())
                .withTotal(getTotal(offer))
                .withSubtotal(getTotal(offer))
                .withDeletedStatus(
                        Objects.isNull(offer.getId())
                                ? DeletedStatus.builder().id(DeletedStatusEnum.ACTIVE.getId())
                                .name("Active").build()
                                : offer.getDeletedStatus()
                )
                .withPaymentRequired(Objects.nonNull(offer.getPaymentRequired()) ? offer.getPaymentRequired() : false)
                .withExpirationTime(getExpirationTime(offer))
                .withRestricted(offer.getRestricted())
                : offerService.save(buildNumber(buildVersion(offer))
                .withDeletedStatus(
                        Objects.isNull(offer.getId())
                                ? DeletedStatus.builder().id(DeletedStatusEnum.ACTIVE.getId())
                                .name("Active").build()
                                : offer.getDeletedStatus()
                ));
        return fromDDBB
                .withGlobalStatus(OfferGLobalStatusEnum.WORKING)
                .withStatus(
                        Objects.isNull(offer.getId())
                                ? OfferStatusEnum.OPEN
                                : fromDDBB.getStatus()
                )
                .withRestricted(fromDDBB.getRestricted())
                .withCustomer(fromDDBB.getCustomer())
                .withDefaultExpiration(offer.getDefaultExpiration())
                .withExpirationTime(getExpirationTime(fromDDBB))
                .withTextToClient(offer.getTextToClient())
                .withProducts(fromDDBB.getProducts())
                .withSubtotal(getTotal(offer))
                .withTotal(getTotal(offer))
                .withCreationTime(
                        Objects.isNull(offer.getId())
                                ? LocalDateTime.now()
                                : fromDDBB.getCreationTime()
                )
                .withModificationTime(
                        Objects.nonNull(offer.getId())
                                ? LocalDateTime.now()
                                : null
                )
                .withDeletedStatus(
                        Objects.isNull(offer.getId())
                                ? DeletedStatus.builder().id(DeletedStatusEnum.ACTIVE.getId())
                                .name("Active").build()
                                : offer.getDeletedStatus()
                )
                .withCreationUser(
                        Objects.isNull(offer.getId())
                                ? securityManagementService.findCurrentUser()
                                : fromDDBB.getCreationUser()
                )
                .withNumber(fromDDBB.getNumber())
                .withModificationUser(
                        Objects.nonNull(offer.getId())
                                ? securityManagementService.findCurrentUser()
                                : fromDDBB.getModificationUser()
                );
    }

    private Offer checkStatusAndGetById(final Offer offer) {
        final Offer byId = offerService.findById(offer.getId());

        if (byId.getGlobalStatus() != OfferGLobalStatusEnum.WORKING) {
            throw new BusinessRuleException("Only can be updated opened offers");
        }

        return byId;
    }

    private Offer buildNumber(final Offer offer) {
        if (Objects.isNull(offer.getId()) && Objects.isNull(offer.getNumber())) {
            return offer.withNumber(offerService.getCurrentNumber());
        }
        return offer;
    }

    private Offer buildVersion(final Offer offer) {
        if (Objects.isNull(offer.getId())) {
            if (Objects.isNull(offer.getNumber())) {
                return offer.withVersion(1);
            } else {
                return offer.withVersion(offerService.getCurrentVersion(offer.getNumber()));
            }
        }
        return offer;
    }

    private void validations(final Offer offer) {
        validateNumber(offer);
        validateClient(offer);
        validatePreOffer(offer);
        //validateProducts(offer);
        //validateSubTotal(offer);
        //validateDiscount(offer);
        validateStatus(offer);
    }

    private void validationSent(final Offer offer) {
        validateStatus(offer);
    }

    private void validateNumber(final Offer offer) {
        if (Objects.isNull(offer.getId()) && Objects.nonNull(offer.getNumber())) {
            if (!offerService.existsByNumber(offer.getNumber())) {
                throw new BusinessRuleException("Order Number not exists");
            }
        }
    }

    private void validatePreOffer(final Offer offer) {
        if (Objects.nonNull(offer.getPreOffer()) && Objects.nonNull(offer.getPreOffer().getId())) {
            preOfferService.existsById(offer.getPreOffer().getId());
        }
    }

    private void validateClient(final Offer offer) {
        if (Objects.isNull(offer.getCustomer()) || Objects.isNull(offer.getCustomer().getId())) {
            throw new FieldIsMissingException(CommonConstants.CUSTOMERS_FIELD);
        }
        customerService.existsById(offer.getCustomer().getId());
    }

    private void validateDiscount(final Offer Offer) {
        if (Objects.nonNull(Offer.getDiscount())) {
            if (Objects.isNull(Offer.getDiscount().getValue())) {
                throw new BusinessRuleException("When set discount, value is mandatory");
            }
            if (Objects.isNull(Offer.getDiscount().getType())) {
                throw new BusinessRuleException("When set discount, type is mandatory");
            }
            if (DiscountTypeEnum.PERCENTAGE.name().equals(Offer.getDiscount().getType())
                    && (Offer.getDiscount().getValue().intValue() < 1
                    || Offer.getDiscount().getValue().intValue() > 100)) {
                throw new BusinessRuleException("Discount value should be between 1 and 100");
            }
            if (DiscountTypeEnum.FIXED.name().equals(Offer.getDiscount().getType())
                    && Offer.getDiscount().getValue().compareTo(getSubtotal(Offer)) > 0) {
                throw new BusinessRuleException("Discount value cannot greater than subTotal value");
            }
        }
    }

    private void validateStatus(final Offer offer) {
        if (Objects.nonNull(offer)) {
            if (OfferGLobalStatusEnum.ACCEPTED == offer.getGlobalStatus()) {
                throw new BusinessRuleException("Cannot create a new version. Current number is accepted.");
            }

            if (offer.getGlobalStatus() == OfferGLobalStatusEnum.WORKING
                    && offer.getStatus() == OfferStatusEnum.OPEN) {
                throw new BusinessRuleException("The current version status is Open." +
                        " Please keep editing the current version or generate a PDF before creating a new version.");
            }
        }
    }

    private void validateProducts(final Offer offer) {
        if (CollectionUtils.isEmpty(offer.getProducts())) {
            throw new BusinessRuleException("Products List is mandatory");
        }
        if (offer.getProducts().stream()
                .map(BockingProduct::getProduct).map(Product::getId).anyMatch(Objects::isNull)) {
            throw new BusinessRuleException("When set product, its id is required");
        }
        offer.getProducts().forEach(productPrice -> productService.existsById(productPrice.getProduct().getId()));
    }

    private BigDecimal getSubtotal(final Offer offer) {
       /* if (Objects.nonNull(preOffer.getSubtotal())) {
            return preOffer.getSubtotal();
        }*/
        return plusProductPrices(offer) == null ? BigDecimal.ZERO : plusProductPrices(offer);
    }

    private BigDecimal getTotal(final Offer offer) {
        return plusProductTotal(offer) == null ? BigDecimal.ZERO : plusProductTotal(offer)
                .setScale(2, RoundingMode.HALF_EVEN);
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

    private static BigDecimal plusProductTotalOffer(final List<BockingProduct> bockingProducts) {
        return !CollectionUtils.isEmpty(bockingProducts) ? bockingProducts.stream()
                .map(bockingProduct -> bockingProduct.getTotalWithVat())
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add) : BigDecimal.ZERO;
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

    @Transactional
    public List<Offer> findAll(final OfferSpecification specification,
                               final Integer page, final Integer size, final String direction, final String attribute) {
        final Direction directionEnum =
                Arrays.stream(Direction.values()).anyMatch(v -> v.name().equalsIgnoreCase(direction))
                        ? Direction.fromString(direction)
                        : Direction.DESC;
        final Sort sort = Sort.by(directionEnum, attribute);
        final Pageable pageable = PageRequest.of(page, size, sort);
        return offerService.findAll(specification, pageable)
                .stream()
                .collect(Collectors.toList());
    }

    public void deleteById(final Integer id) {
        final Offer offer = offerService.findById(id);
        checkIfHasAssociatedOrder(offer);

        if (!CollectionUtils.isEmpty(offer.getFiles())) {
            offer.getFiles().forEach(file -> fileService.deleteById(file.getId()));
        }
        offerService.deleteById(offer.getId());
    }

    @Transactional
    public void deleteProductFileOffer(final Integer bockingProductId, final Integer offerId, final Integer fileId) {
        validId(offerId);
        validateBockingProduct(bockingProductId);
        Offer offer = offerService.findById(offerId);
        validateProductAssociateOffer(bockingProductId, fileId, offer);
    }

    private void validateProductAssociateOffer(Integer bockingProductId, Integer fileId, Offer offer) {
        if (!CollectionUtils.isEmpty(offer.getProducts())) {
            for (BockingProduct bockingProduct : offer.getProducts()) {
                getProductBockingId(bockingProductId, fileId, getBockingProduct(bockingProduct, offer));
            }
        }
    }

    public void deleteProductOffer(final Integer productId, final Integer offerId) {
        validId(offerId);
        validateProductBocking(productId);
        Offer offer = offerService.findById(offerId);
        if (!CollectionUtils.isEmpty(offer.getProducts()) &&
                offer.getProducts().stream().anyMatch(productPrice ->
                        productPrice.getId().intValue() == productId)) {
            offer.getProducts().forEach(productPrice -> priceRepository.delete(productId));
        } else {
            throw new BusinessRuleException("There is no product associated with the offer this id:" + productId);
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
                validateFileFoundBockingProduct(fileId, fileFound);
                bockingProduct.setFiles(filesCopy);
                priceRepository.save(priceMapper.modelToEntity(bockingProduct));
            } else {
                throw new BusinessRuleException("It does not have a " +
                        "file associated with the BockingProduct");
            }
        }
    }

    private void validateProductBocking(Integer bockingProductId) {
        if (!priceRepository.existsById(bockingProductId)) {
            throw new BockingProductNotFoundException(bockingProductId);
        }
    }

    public void deleteStatus(final Integer id) {
        final Offer offer = offerService.findById(id);
        checkIfHasAssociatedOrder(offer);
        offerService.deleteStatus(offer.getId());
    }

    public void deleteByNumber(final Integer number) {
        final List<Offer> offers = offerService.findByNumber(number);
        if (CollectionUtils.isEmpty(offers)) {
            throw new BusinessRuleException("There are no offers for this number " + number);
        }
        offers.forEach(this::checkIfHasAssociatedOrder);
        offers.forEach(offer -> {
            if (!CollectionUtils.isEmpty(offer.getFiles())) {
                offer.getFiles().forEach(file -> fileService.deleteById(file.getId()));
            }
            offerService.deleteById(offer.getId());
        });
    }

    private void checkIfHasAssociatedOrder(final Offer offer) {
        final List<Order> byOffer = orderService.findByOffer(offer);
        if (!CollectionUtils.isEmpty(byOffer)) {
            final String message = String
                    .format(
                            "The offer id %s, number %s, version %s cannot be deleted. "
                                    + "It is related to %s orders",
                            offer.getId(), offer.getNumber(), offer.getVersion(),
                            byOffer.size()
                    );
            throw new BusinessRuleException(message);
        }
    }

    @Transactional
    public Map<String, String> getPdfById(final Integer id) throws IOException {
        Offer offer = offerService.findById(id);
        log.info("Generate Pdf by Offer id :{}", id);
        if (!(offer.getStatus() == OfferStatusEnum.OPEN && offer.getGlobalStatus() == OfferGLobalStatusEnum.WORKING
                && (Objects.isNull(offer.getExpirationTime()) || offer.getExpirationTime().isAfter(LocalDateTime.now())))) {
            throw new BusinessRuleException("Cannot generate pdf. Offer isn't open or is expired");
        }

        final String reportUrl = reportService.exportOfferPdf(offer);

        changeStatus(offer.withPdfUrl(reportUrl), OfferStatusEnum.CONFIRMED);
        return Map.of("url", reportUrl);
    }

    @Transactional
    public void closeById(final Integer id) {
        final Offer offer = offerService.findById(id);
        offerService.findByNumber(offer.getNumber())
                .stream()
                .peek(o -> {
                    if (OfferStatusEnum.ACCEPTED == o.getStatus()) {
                        throw new BusinessRuleException(
                                String.format("Offer id %s is accepted. Offers cannot be closed", offer.getId())
                        );
                    }
                })
                .forEach(o -> changeStatus(o, OfferStatusEnum.CLOSED));
    }

    private void changeStatus(final Offer offer,
                              final OfferStatusEnum status) {
        offerService.save(offer
                .withStatus(status.equals(OfferStatusEnum.CONFIRMED) ? OfferStatusEnum.CONFIRMED : offer.getStatus())
                .withGlobalStatus(status.getId() == OfferStatusEnum.CLOSED.getId()
                        ? OfferGLobalStatusEnum.DECLINED : OfferGLobalStatusEnum.WORKING)
                .withModificationUser(securityManagementService.findCurrentUser())
                .withModificationTime(LocalDateTime.now()));
    }

    private File getFileMessage(final String url, User currentUser, final Offer offer) {
        return fileService.saveOffer(
                File.builder()
                        .url(url)
                        .extension("pdf")
                        .name(offer.getName())
                        //.offers(Objects.nonNull(offer) ? List.of(offer) : null)
                        .persons(Objects.nonNull(offer.getCustomer()) ? List.of(offer.getCustomer().getPerson()) : null)
                        .status(DeletedStatus.builder().id(DeletedStatusEnum.ACTIVE.getId()).build())
                        .creationUser(Objects.isNull(currentUser) ? securityManagementService.findCurrentUser() : currentUser)
                        .creationTime(LocalDateTime.now())
                        .build()
        );
    }
}
