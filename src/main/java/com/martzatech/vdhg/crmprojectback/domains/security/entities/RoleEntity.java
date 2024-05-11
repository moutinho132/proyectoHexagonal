package com.martzatech.vdhg.crmprojectback.domains.security.entities;

import com.martzatech.vdhg.crmprojectback.domains.customers.entities.DeletedEntity;
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
import lombok.With;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "t_roles")
public class RoleEntity implements Serializable {

  @Serial
  private static final long serialVersionUID = 2873660117313592053L;

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "name", nullable = false, length = 128)
  private String name;

  @Column(name = "description", length = 512)
  private String description;

  @ManyToOne
  @JoinColumn(name = "department_id", nullable = false)
  private DepartmentEntity department;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "t_roles_permissions",
      joinColumns = @JoinColumn(name = "role_id"),
      inverseJoinColumns = @JoinColumn(name = "permission_id"))
  @With
  private List<PermissionEntity> permissions;

  @Column(name = "creation_time")
  private LocalDateTime creationTime;

  @Column(name = "modification_time")
  private LocalDateTime modificationTime;

  @ManyToOne
  @JoinColumn(name = "deleted_id")
  private DeletedEntity status;

  @ManyToOne
  @JoinColumn(name = "creation_user_id")
  private UserEntity creationUser;

  @ManyToOne
  @JoinColumn(name = "modification_user_id")
  private UserEntity modificationUser;
}
