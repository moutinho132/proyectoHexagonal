package com.martzatech.vdhg.crmprojectback.domains.customers.entities;

import com.martzatech.vdhg.crmprojectback.domains.commons.entities.LanguageEntity;
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
@Table(name = "t_associates")
public class AssociatedEntity implements Serializable {

  @Serial
  private static final long serialVersionUID = 5160393417188058269L;

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "name")
  private String name;

  @Column(name = "surname")
  private String surname;

  @Column(name = "email")
  private String email;

  @Column(name = "position")
  private String position;

  @Column(name = "main_contact")
  private Boolean mainContact;

  @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.REMOVE)
  @JoinColumn(name = "group_account_id")
  private GroupAccountEntity groupAccount;

  @Column(name = "phone_prefix")
  private String phonePrefix;

  @Column(name = "phone_number")
  private String phoneNumber;

  @ManyToOne
  @JoinColumn(name = "preferred_language_id")
  private LanguageEntity preferredLanguage;

  /*@OneToMany(mappedBy = "associated", fetch = FetchType.EAGER,cascade = CascadeType.REMOVE,orphanRemoval = true)
  private List<UserEntity> user;*/

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
