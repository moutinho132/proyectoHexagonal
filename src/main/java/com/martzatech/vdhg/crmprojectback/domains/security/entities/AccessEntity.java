package com.martzatech.vdhg.crmprojectback.domains.security.entities;

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
@Table(name = "t_accesses")
public class AccessEntity implements Serializable {

  @Serial
  private static final long serialVersionUID = -1389055717115359802L;

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "name", nullable = false, length = 128)
  private String name;

  @Column(name = "description", length = 512)
  private String description;

  @ManyToOne
  @JoinColumn(name = "group_id", nullable = false)
  private AccessGroupEntity group;

  @OneToMany(mappedBy = "access", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<PrivilegeEntity> privileges;
}
