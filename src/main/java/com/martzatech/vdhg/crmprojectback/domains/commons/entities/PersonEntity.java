package com.martzatech.vdhg.crmprojectback.domains.commons.entities;

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
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "t_people")
public class PersonEntity implements Serializable {

  @Serial
  private static final long serialVersionUID = 4065082902092960044L;

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "name")
  private String name;

  @Column(name = "surname")
  private String surname;

  @Formula("concat_ws(' ',name,surname)")
  private String fullName;

  @Column(name = "date_of_birth")
  private LocalDate dateOfBirth;

  @ManyToOne
  @JoinColumn(name = "title_id")
  private PersonTitleEntity title;

  @ManyToOne
  @JoinColumn(name = "nationality_id")
  private CountryEntity nationality;

  @ManyToOne
  @JoinColumn(name = "residence_id")
  private CountryEntity residence;

  @ManyToOne
  @JoinColumn(name = "gender_id")
  private GenderEntity gender;

  @ManyToOne
  @JoinColumn(name = "civil_status_id")
  private CivilStatusEntity civilStatus;

  @ManyToOne
  @JoinColumn(name = "preferred_language_id")
  private LanguageEntity preferredLanguage;

  @ManyToOne
  @JoinColumn(name = "deleted_id")
  private DeletedEntity deletedStatus;

  @OneToMany(mappedBy = "person", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<IdentityDocumentEntity> identityDocuments;

  @OneToMany(mappedBy = "person", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<PhoneEntity> phones;

  @OneToMany(mappedBy = "person", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<EmailEntity> emails;

  @OneToMany(mappedBy = "person", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<AddressEntity> addresses;
}
