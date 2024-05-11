package com.martzatech.vdhg.crmprojectback.domains.customers.entities;

import com.martzatech.vdhg.crmprojectback.domains.commons.entities.PersonEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "t_employees")
public class EmployeeEntity implements Serializable {

  @Serial
  private static final long serialVersionUID = 2631538231425614158L;

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "creation_time")
  private LocalDateTime creationTime;

  @ManyToOne
  @JoinColumn(name = "type_id")
  private EmployeeTypeEntity type;

  @OneToOne
  @JoinColumn(name = "person_id")
  private PersonEntity person;
}
