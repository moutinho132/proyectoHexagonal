package com.martzatech.vdhg.crmprojectback.domains.customers.entities;

import com.martzatech.vdhg.crmprojectback.domains.security.entities.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "t_bookings_products_files")
public class ProductFileEntity implements Serializable {

  @Serial
  private static final long serialVersionUID = -8536297222423983696L;

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "url")
  private String url;

  @Column(name = "name")
  private String name;

  @ManyToOne
  @JoinColumn(name = "deleted_id")
  private DeletedEntity status;

  @ManyToOne
  @JoinColumn(name = "product_id")
  public ProductEntity product;

  @ManyToOne
  @JoinColumn(name = "creation_user_id")
  private UserEntity creationUser;

  @ManyToOne
  @JoinColumn(name = "removal_user_id")
  private UserEntity removalUser;

  @Column(name = "creation_time")
  private LocalDateTime creationTime;

  @Column(name = "removal_time")
  private LocalDateTime removalTime;
}
