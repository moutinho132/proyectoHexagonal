package com.martzatech.vdhg.crmprojectback.domains.security.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "t_permissions")
public class PermissionEntity implements Serializable {

  @Serial
  private static final long serialVersionUID = -746365383777946774L;

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "name", nullable = false, length = 128)
  private String name;

  @Column(name = "resource", nullable = false, length = 128)
  private String resource;

  @Column(name = "method", nullable = false, length = 128)
  private String method;

  @Column(name = "field", length = 32)
  private String field;

  @ManyToOne
  @JoinColumn(name = "privilege_id", nullable = false)
  private PrivilegeEntity privilege;
}
