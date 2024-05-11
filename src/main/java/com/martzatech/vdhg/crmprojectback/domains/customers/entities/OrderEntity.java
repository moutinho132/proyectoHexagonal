package com.martzatech.vdhg.crmprojectback.domains.customers.entities;

import com.martzatech.vdhg.crmprojectback.domains.customers.converters.OrderStatusEnumConverter;
import com.martzatech.vdhg.crmprojectback.domains.customers.enums.OrderStatusEnum;
import com.martzatech.vdhg.crmprojectback.domains.security.entities.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "t_bookings_orders")
public class OrderEntity implements Serializable {

  @Serial
  private static final long serialVersionUID = 2873660117313592053L;

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "status")
  @Convert(converter = OrderStatusEnumConverter.class)
  private OrderStatusEnum status;

  @Column(name = "notes")
  private String notes;

  @Column(name = "description")
  private String description;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "offer_id", nullable = false)
  private OfferEntity offer;

  @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
  @JoinColumn(name = "payment_details_id")
  private PaymentDetailsEntity paymentDetails;

  /*@Column(name = "restricted")
  private Boolean restricted;*/

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "deleted_id")
  @With
  private DeletedEntity deletedStatus;

  @Column(name = "pdf_url")
  private String pdfUrl;

  @Column(name = "pdf_url_internal")
  private String pdfUrlInternal;


  @ManyToOne(fetch = FetchType.LAZY )
  @JoinColumn(name = "creation_user_id")
  private UserEntity creationUser;

  @ManyToOne(fetch = FetchType.LAZY )
  @JoinColumn(name = "modification_user_id")
  private UserEntity modificationUser;

  @Column(name = "creation_time")
  private LocalDateTime creationTime;

  @Column(name = "modification_time")
  private LocalDateTime modificationTime;
}
