package com.martzatech.vdhg.crmprojectback.domains.customers.entities;

import jakarta.persistence.*;

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
@Table(name = "t_companies")
public class CompanyEntity implements Serializable {

  @Serial
  private static final long serialVersionUID = -9090082987431634340L;
  
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "name")
  private String name;
}
