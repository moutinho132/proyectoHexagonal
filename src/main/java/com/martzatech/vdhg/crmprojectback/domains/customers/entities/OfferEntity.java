package com.martzatech.vdhg.crmprojectback.domains.customers.entities;

import com.martzatech.vdhg.crmprojectback.domains.customers.converters.OfferGlobalStatusEnumConverter;
import com.martzatech.vdhg.crmprojectback.domains.customers.converters.OfferStatusEnumConverter;
import com.martzatech.vdhg.crmprojectback.domains.customers.enums.OfferGLobalStatusEnum;
import com.martzatech.vdhg.crmprojectback.domains.customers.enums.OfferStatusEnum;
import com.martzatech.vdhg.crmprojectback.domains.security.entities.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

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
@Table(name = "t_bookings_offers")
public class OfferEntity implements Serializable {

  @Serial
  private static final long serialVersionUID = 5131122998405468595L;

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "offer_number")
  private Integer number;

  @Column(name = "offer_version")
  private Integer version;

  @Column(name = "status")
  @Convert(converter = OfferStatusEnumConverter.class)
  private OfferStatusEnum status;

  @Column(name = "name")
  private String name;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "pre_offer_id")
  private PreOfferEntity preOffer;

  @Column(name = "description")
  private String description;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "customer_id")
  private CustomerEntity customer;

  @OneToMany(mappedBy = "offer", fetch = FetchType.EAGER, orphanRemoval = true)
  private List<BockingProductEntity> products;

  /*@ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
          name = "t_bookings_offer_files",
          joinColumns = @JoinColumn(name = "offer_id"),
          inverseJoinColumns = @JoinColumn(name = "file_id"))
  @With
  private List<FileEntity> files;*/

  @Column(name = "currency")
  private String currency;

  @Column(name = "subtotal")
  private BigDecimal subtotal;

  @Column(name = "total")
  private BigDecimal total;

  @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
  @JoinColumn(name = "discount_id")
  private DiscountEntity discount;

  @Column(name = "payment_required")
  private Boolean paymentRequired;

  @Column(name = "default_expiration")
  private Boolean defaultExpiration;

  @Column(name = "expiration_time")
  private LocalDateTime expirationTime;

  @Formula("expiration_time > now()")
  private Boolean active;

  @Column(name = "restricted")
  private Boolean restricted;

  @Column(name = "text_client")
  private String textToClient;

  @ManyToOne
  @JoinColumn(name = "deleted_id")
  private DeletedEntity deletedStatus;

  @Column(name = "global_status")
  @Convert(converter = OfferGlobalStatusEnumConverter.class)
  private OfferGLobalStatusEnum globalStatus;

  @Column(name = "pdf_url")
  private String pdfUrl;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "creation_user_id")
  private UserEntity creationUser;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "modification_user_id")
  private UserEntity modificationUser;

  @Column(name = "creation_time")
  private LocalDateTime creationTime;

  @Column(name = "modification_time")
  private LocalDateTime modificationTime;
}
