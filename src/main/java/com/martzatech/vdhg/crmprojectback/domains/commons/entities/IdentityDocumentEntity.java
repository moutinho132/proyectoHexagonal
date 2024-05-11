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
@Table(name = "t_identity_documents")
public class IdentityDocumentEntity implements Serializable {

  @Serial
  private static final long serialVersionUID = 768837406400555537L;

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "value")
  private String value;

  @ManyToOne
  @JoinColumn(name = "country_id")
  private CountryEntity country;

  @ManyToOne
  @JoinColumn(name = "identity_document_type_id")
  private IdentityDocumentTypeEntity type;

  @ManyToOne
  @JoinColumn(name = "deleted_id")
  private DeletedEntity deletedStatus;

  @Column(name = "creation_time")
  private LocalDateTime creationTime;

  @Column(name = "modification_time")
  private LocalDateTime modificationTime;

  @ManyToOne
  @JoinColumn(name = "person_id")
  @With
  private PersonEntity person;
}
