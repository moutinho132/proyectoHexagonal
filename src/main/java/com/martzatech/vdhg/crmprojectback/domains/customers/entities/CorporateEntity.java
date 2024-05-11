package com.martzatech.vdhg.crmprojectback.domains.customers.entities;

import com.martzatech.vdhg.crmprojectback.domains.security.entities.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "t_corporates")
public class CorporateEntity implements Serializable {

  @Serial
  private static final long serialVersionUID = 1461890413167940847L;

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "main_contact_id")
  private CustomerEntity mainContact;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "t_corporates_customers",
      joinColumns = @JoinColumn(name = "corporate_id"),
      inverseJoinColumns = @JoinColumn(name = "customer_id"))
  private List<CustomerEntity> customers;

  @ManyToOne
  @JoinColumn(name = "deleted_id")
  private DeletedEntity status;

  @Column(name = "name")
  private String name;

  @Column(name = "alias")
  private String alias;

  @Column(name = "industry")
  private String industry;

  @Column(name = "vat")
  private String vat;

  @Column(name = "email")
  private String email;

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
