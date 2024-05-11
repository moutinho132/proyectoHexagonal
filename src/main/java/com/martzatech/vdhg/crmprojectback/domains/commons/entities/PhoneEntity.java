package com.martzatech.vdhg.crmprojectback.domains.commons.entities;

import com.martzatech.vdhg.crmprojectback.domains.customers.entities.DeletedEntity;
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
import java.time.LocalDateTime;
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
@Table(name = "t_phones")
public class PhoneEntity implements Serializable {

  @Serial
  private static final long serialVersionUID = -3703520904372562791L;

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "code")
  private String code;

  @Column(name = "value")
  private String value;

  @Column(name = "valid")
  private Boolean valid;

  @ManyToOne
  @JoinColumn(name = "attribute_type_id")
  private AttributeTypeEntity attributeType;

  @ManyToOne
  @JoinColumn(name = "deleted_id")
  @With
  private DeletedEntity deletedStatus;

  @Column(name = "check_time")
  private LocalDateTime checkTime;

  @Column(name = "creation_time")
  private LocalDateTime creationTime;

  @Column(name = "modification_time")
  private LocalDateTime modificationTime;

  @ManyToOne
  @JoinColumn(name = "person_id")
  @With
  private PersonEntity person;
}
