package com.martzatech.vdhg.crmprojectback.domains.security.entities;

import com.martzatech.vdhg.crmprojectback.domains.customers.entities.DeletedEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "t_departments")
public class DepartmentEntity implements Serializable {

  @Serial
  private static final long serialVersionUID = -714495844437830692L;

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "name", nullable = false, length = 128)
  private String name;

  @Column(name = "description", nullable = false, length = 512)
  private String description;

  @ManyToOne
  @JoinColumn(name = "subsidiary_id")
  private SubsidiaryEntity subsidiary;

  @OneToMany(mappedBy = "department", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<RoleEntity> roles;

  @Column(name = "creation_time")
  private LocalDateTime creationTime;

  @Column(name = "modification_time")
  private LocalDateTime modificationTime;

  @ManyToOne
  @JoinColumn(name = "deleted_id")
  @With
  private DeletedEntity status;

  @ManyToOne
  @JoinColumn(name = "creation_user_id")
  private UserEntity creationUser;

  @ManyToOne
  @JoinColumn(name = "modification_user_id")
  private UserEntity modificationUser;
}
