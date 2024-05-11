package com.martzatech.vdhg.crmprojectback.domains.customers.entities;

import com.martzatech.vdhg.crmprojectback.domains.chat.entities.FileEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "t_bookings_product")
public class BockingProductEntity implements Serializable {

  @Serial
  private static final long serialVersionUID = -3353961142258561869L;

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "product_id")
  private ProductEntity product;

  @Column(name = "requires_payment")
  private Boolean requiresPayment;

  @Column(name = "show_price")
  private Boolean showPrice;


  @Column(name = "default_base_price")
  private Boolean defaultBasePrice;

  @Column(name = "base_price")
  private BigDecimal basePrice;

  @Column(name = "vat_percentage")
  private BigDecimal vatPercentage;

  @Column(name = "vat_value")
  private BigDecimal vatValue;

  @ManyToMany(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
  @JoinTable(
          name = "t_bookings_products_files",
          joinColumns = @JoinColumn(name = "bocking_product_id"),
          inverseJoinColumns = @JoinColumn(name = "file_id"))
  @With
  private List<FileEntity> files;

  @Column(name = "default_commission")
  private Boolean defaultCommission;

  @Column(name = "commission_percentage")
  private BigDecimal commissionPercentage;

  @Column(name = "commission_value")
  private BigDecimal commissionValue;

  @Column(name = "price_with_vat")
  private BigDecimal totalWithVat;

  @Column(name = "price_with_commission")
  private BigDecimal totalWithCommission;

  @Column(name = "default_description")
  private Boolean defaultDescription;

  @Column(name = "default_vat")
  private Boolean defaultVat;

  @Column(name = "description")
  private String description;

  @Column(name = "default_marketing")
  private Boolean defaultMarketing;

  @Column(name = "marketing")
  private String marketing;

  @Column(name = "default_availability_from")
  private Boolean defaultAvailabilityFrom;

  @Column(name = "availability_from")
  private LocalDateTime availabilityFrom;

  @Column(name = "default_availability_to")
  private Boolean defaultAvailabilityTo;

  @Column(name = "availability_to")
  private LocalDateTime availabilityTo;

  @Column(name = "paymment_reference")
  private String paymentReference;

  @Column(name = "default_payment_method")
  private Boolean defaultPaymentMethod;

  @Column(name = "payment_method")
  private String paymentMethod;

  @Column(name = "default_payment_details")
  private Boolean defaultPaymentDetails;

  @Column(name = "payment_details")
  private String paymentDetails;

  @ManyToOne
  @JoinColumn(name = "pre_offer_id")
  private PreOfferEntity preOffer;

  @ManyToOne
  @JoinColumn(name = "offer_id")
  private OfferEntity offer;
}
