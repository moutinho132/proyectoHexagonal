package com.martzatech.vdhg.crmprojectback.domains.customers.entities;

import com.martzatech.vdhg.crmprojectback.domains.security.entities.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "t_group_accounts")
public class GroupAccountEntity implements Serializable {

  @Serial
  private static final long serialVersionUID = 8694600644420583295L;

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "name")
  private String name;

  @Column(name = "email")
  private String email;

  @Column(name = "industry")
  private String industry;

  @Column(name = "vat")
  private String vat;

  @Column(name = "alias")
  private String alias;

  @ManyToOne
  @JoinColumn(name = "owner_id")
  private CustomerEntity owner;

  @Singular
  @OneToMany(mappedBy = "groupAccount", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = false)
  private List<AssociatedEntity> associates = new ArrayList<>();

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

  @ManyToOne
  @JoinColumn(name = "deleted_id")
  private DeletedEntity status;
}
