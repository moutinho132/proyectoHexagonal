package com.martzatech.vdhg.crmprojectback.domains.customers.entities;

import com.martzatech.vdhg.crmprojectback.domains.customers.converters.OfferStatusEnumConverter;
import com.martzatech.vdhg.crmprojectback.domains.customers.converters.PreOfferStatusEnumConverter;
import com.martzatech.vdhg.crmprojectback.domains.customers.enums.OfferStatusEnum;
import com.martzatech.vdhg.crmprojectback.domains.customers.enums.PreOfferStatusEnum;
import com.martzatech.vdhg.crmprojectback.domains.security.entities.UserEntity;
import jakarta.persistence.*;
import lombok.*;
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
@Table(name = "t_bookings_pre_offers")
public class PreOfferEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -1195883458875961093L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "pre_offer_number")
    private Integer number;

    @Column(name = "pre_offer_version")
    private Integer version;

    @Column(name = "status")
    @Convert(converter = OfferStatusEnumConverter.class)
    private OfferStatusEnum status;

    @Column(name = "global_status")
    @Convert(converter = PreOfferStatusEnumConverter.class)
    private PreOfferStatusEnum globalStatus;

    @Column(name = "name")
    private String name;

    @Column(name = "default_expiration")
    private Boolean defaultExpiration;

    @Column(name = "text_client")
    private String textToClient;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "preOffer", fetch = FetchType.EAGER, orphanRemoval = true)
    private List<BockingProductEntity> products;

    @Column(name = "currency")
    private String currency;

    @Column(name = "subtotal",precision = 11, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "total",precision = 11, scale = 2)
    private BigDecimal total;

    /*@OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "discount_id")
    private DiscountEntity discount;*/

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "t_bookings_pre_offer_customer",
            joinColumns = @JoinColumn(name = "pre_offer_id"),
            inverseJoinColumns = @JoinColumn(name = "customer_id"))
    @With
    private List<CustomerEntity> customers;

    @Column(name = "payment_required")
    private Boolean paymentRequired;

    @Column(name = "expiration_time")
    private LocalDateTime expirationTime;

    @Formula("expiration_time > now()")
    private Boolean active;

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
