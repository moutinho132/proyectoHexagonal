package com.martzatech.vdhg.crmprojectback.domains.customers.entities;

import com.martzatech.vdhg.crmprojectback.domains.commons.entities.PersonEntity;
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
@Table(name = "t_customers")
public class CustomerEntity implements Serializable {

  @Serial
  private static final long serialVersionUID = -5560271806265311713L;

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "alias")
  private String alias;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "membership_id")
  private MembershipEntity membership;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "company_id")
  private CompanyEntity company;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "person_id")
  private PersonEntity person;

  @Column(name = "reference")
  private String reference;

  @Column(name = "loyalty_points")
  private Integer loyaltyPoints;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "lead_id")
  private LeadEntity lead;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "status_id")
  private CustomerStatusEntity status;


  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "creation_type_id")
  private CreationTypeEntity creationType;

  @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
  @JoinColumn(name = "payment_details_id")
  private PaymentDetailsEntity paymentDetails;

  @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER,cascade = CascadeType.ALL)
  private List<UserEntity> user;

  @OneToOne(cascade = CascadeType.REMOVE, mappedBy = "owner")
  private GroupAccountEntity groupAccount;

  @Formula("(select ifnull(sum(o.total),0) from t_bookings_offers o where o.customer_id = id and o.status in (1,2))")
  @Basic(fetch= FetchType.EAGER)
  private BigDecimal totalSpendAmount;

  @ManyToOne
  @JoinColumn(name = "deleted_id")
  @With
  private DeletedEntity deletedStatus;

  @ManyToOne
  @JoinColumn(name = "creation_user_id")
  private UserEntity creationUser;

  @ManyToOne
  @JoinColumn(name = "modification_user_id")
  private UserEntity modificationUser;

  @Column(name = "creation_time")
  private LocalDateTime creationTime;

  @Column(name = "modification_time")
  private LocalDateTime modificationTime;
}
