package com.martzatech.vdhg.crmprojectback.application.helper;

import com.martzatech.vdhg.crmprojectback.domains.customers.models.BockingProduct;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Offer;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.PreOfferProduct;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Product;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Objects;

public class BockingProductHelper {

    public static BigDecimal getBasePrice(final BockingProduct productPrice) {
        if(Objects.nonNull(productPrice.getRequiresPayment()) && productPrice.getRequiresPayment()==Boolean.TRUE){
            if (productPrice.getDefaultBasePrice() == Boolean.TRUE && Objects.nonNull(productPrice.getProduct().getBasePrice())) {
                return productPrice.getProduct().getBasePrice().setScale(2, RoundingMode.HALF_EVEN);
            } else {
                if (Objects.nonNull(productPrice.getBasePrice())) {
                    return productPrice.getBasePrice().setScale(2, RoundingMode.HALF_EVEN);
                }
            }
        }
        return BigDecimal.ZERO;
    }

    public static BigDecimal plusProductTotalCommission(final Offer offer) {
        return !CollectionUtils.isEmpty(offer.getProducts()) ? offer.getProducts().stream()
                .map(bockingProduct -> bockingProduct.getCommissionValue())
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add) : BigDecimal.ZERO;
    }

    public static String getDescription(final BockingProduct productPrice) {
        return productPrice.getDefaultDescription() == Boolean.TRUE ? productPrice.getProduct().getDescription() : productPrice.getDescription();
    }

    public static String getMarketing(final BockingProduct productPrice) {
        return productPrice.getDefaultMarketing() == Boolean.TRUE ? productPrice.getProduct().getMarketing() : productPrice.getMarketing();
    }

    public static LocalDateTime getAvailabilityTo(final BockingProduct productPrice) {
        return productPrice.getDefaultAvailabilityTo() == Boolean.TRUE ? productPrice.getProduct().getAvailabilityTo() : productPrice.getAvailabilityTo();
    }

    public static LocalDateTime getAvailabilityFrom(final BockingProduct productPrice) {
        return productPrice.getDefaultAvailabilityFrom() == Boolean.TRUE ? productPrice.getProduct().getAvailabilityFrom() : productPrice.getAvailabilityFrom();
    }

    public static BigDecimal getResultTotalWithVat(final BockingProduct productPrice) {
        BigDecimal resultCommission = getResultTotalWithCommission(productPrice, productPrice.getProduct());
        return resultCommission.add(getVatValue(productPrice));
    }

    public static BigDecimal getCalculateTotalBockingProduct(final PreOfferProduct productPrice, final Product product) {
        BigDecimal price = getResultPriceBockingProduct(productPrice, product);
        BigDecimal commission;

        if (productPrice.getDefaultCommission() == Boolean.TRUE) {
            if (Objects.nonNull(product) && product.getDefaultCommission() == Boolean.TRUE) {
                commission = Objects.nonNull(product.getVendor()) ?
                        product.getVendor().getCommission() : BigDecimal.ZERO;
            } else {
                commission = Objects.nonNull(product.getProductCommission())
                        ? product.getProductCommission() : BigDecimal.ZERO;
            }
        } else {
            commission = Objects.nonNull(productPrice.getCommission()) ? productPrice.getCommission() : BigDecimal.ZERO;
        }
        return price.add((price.multiply(commission).divide(BigDecimal.valueOf(100))))
                .setScale(2, RoundingMode.HALF_EVEN);

    }

    public static BigDecimal getCommission(final BockingProduct productPrice) {
        BigDecimal commission = BigDecimal.ZERO;

        if (productPrice.getDefaultCommission() == Boolean.TRUE) {
            if (productPrice.getProduct().getDefaultCommission() == Boolean.TRUE && Objects.nonNull(productPrice.getProduct().getVendor())) {
                commission = productPrice.getProduct().getVendor().getCommission();
            } else {
                if (Objects.nonNull(productPrice.getProduct().getProductCommission())) {
                    commission = productPrice.getProduct().getProductCommission();
                }
            }
        } else {
            if (Objects.nonNull(productPrice.getCommissionPercentage())) {
                commission = productPrice.getCommissionPercentage();
            }
        }

       /* return productPrice.getDefaultCommission() == Boolean.TRUE
                ? productPrice.getProduct().getDefaultCommission() == Boolean.TRUE
                ? Objects.nonNull(productPrice.getProduct().getVendor())
                ? productPrice.getProduct().getVendor().getCommission():null
                : productPrice.getProduct().getProductCommission()
                : productPrice.getCommission();*/
        return commission;
    }

    public static BigDecimal getResultTotalWithCommission(BockingProduct productPrice, Product product) {
        BigDecimal basePrice = BigDecimal.ZERO;
        basePrice = getBasePrice(productPrice, product, basePrice);
        var commissionValue = getCommissionValue(productPrice);
        return basePrice//200 +40
                .add(commissionValue) // 40
                .setScale(2, RoundingMode.HALF_EVEN);
    }

    private static BigDecimal getBasePrice(BockingProduct productPrice, Product product, BigDecimal basePrice) {
        if (productPrice.getDefaultBasePrice() == Boolean.TRUE && Objects.nonNull(product.getBasePrice())) {
            basePrice = product.getBasePrice();
        } else {
            if (Objects.nonNull(productPrice.getBasePrice())) {
                basePrice = productPrice.getBasePrice();
            }
        }
        return basePrice;
    }

    public static BigDecimal getResultPriceBockingProduct(PreOfferProduct preOfferProduct, Product product) {
        BigDecimal basePrice = BigDecimal.ZERO;
        basePrice = getBasePrice(preOfferProduct, product, basePrice);
        BigDecimal vat = getVatCalculationBockingProduct(preOfferProduct, product);
        return Objects.isNull(basePrice) ? BigDecimal.ZERO : basePrice
                .add((basePrice
                        .multiply(vat)).divide(BigDecimal.valueOf(100))).setScale(2, RoundingMode.HALF_EVEN);
    }

    private static BigDecimal getBasePrice(PreOfferProduct preOfferProduct, Product product, BigDecimal basePrice) {
        if (preOfferProduct.getDefaultBasePrice() == Boolean.TRUE) {
            basePrice = product.getBasePrice();
        } else {
            if (Objects.nonNull(preOfferProduct.getBasePrice())) {
                basePrice = preOfferProduct.getBasePrice();
            } else {
                basePrice = BigDecimal.ZERO;
            }
        }
        return basePrice;
    }

    private static BigDecimal getVatCalculationBockingProduct(PreOfferProduct productPrice, Product product) {
        BigDecimal vat;
        if (productPrice.getDefaultVat() == Boolean.TRUE) {
            if (product.getDefaultVat() == Boolean.TRUE) {
                vat = Objects.isNull(product.getVendor()) ?
                        BigDecimal.ZERO : product.getVendor().getVat();
            } else {
                vat = Objects.nonNull(product.getProductVat())
                        ? product.getProductVat() : BigDecimal.ZERO;
            }
        } else {
            vat = Objects.nonNull(productPrice.getVat()) ? productPrice.getVat() : BigDecimal.ZERO;
        }
        return vat;
    }

    private static BigDecimal getVatCalculation(BockingProduct productPrice) {
        BigDecimal vat;
        if (productPrice.getDefaultVat() == Boolean.TRUE) {
            if (productPrice.getProduct().getDefaultVat() == Boolean.TRUE) {
                vat = Objects.isNull(productPrice.getProduct().getVendor()) ?
                        BigDecimal.ZERO : productPrice.getProduct().getVendor().getVat();
            } else {
                vat = Objects.nonNull(productPrice.getProduct().getProductVat())
                        ? productPrice.getProduct().getProductVat() : BigDecimal.ZERO;
            }
        } else {
            vat = Objects.nonNull(productPrice.getVatPercentage()) ? productPrice.getVatPercentage() : BigDecimal.ZERO;
        }
        return vat;
    }

    public static BigDecimal getVat(final BockingProduct productPrice) {
        return productPrice.getDefaultVat() == Boolean.TRUE
                ? productPrice.getProduct().getDefaultVat() == Boolean.TRUE
                ? Objects.nonNull(productPrice.getProduct().getVendor()) ? productPrice.getProduct().getVendor().getVat() : null
                : productPrice.getProduct().getProductVat()
                : productPrice.getVatPercentage();
    }


    public static BigDecimal getCommissionValue(BockingProduct productPrice) { // TODO:Validar a primera hora
        return Objects.nonNull(productPrice.getBasePrice())
                && Objects.nonNull(productPrice.getCommissionPercentage()) ?
                productPrice.getBasePrice().multiply(productPrice.getCommissionPercentage())
                        .divide(BigDecimal.valueOf(100))
                : BigDecimal.ZERO;
    }

    public static BigDecimal getVatValue(BockingProduct productPrice) {
        BigDecimal resultTotalWithCommission = getResultTotalWithCommission(productPrice, productPrice.getProduct());
        return  Objects.nonNull(productPrice.getVatPercentage()) ?
                resultTotalWithCommission.multiply(productPrice.getVatPercentage())
                        .divide(BigDecimal.valueOf(100)) : BigDecimal.ZERO;
    }

}
