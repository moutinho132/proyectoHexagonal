package com.martzatech.vdhg.crmprojectback.domains.customers.entities;

import com.martzatech.vdhg.crmprojectback.domains.customers.converters.NoteTypeEnumConverter;
import com.martzatech.vdhg.crmprojectback.domains.customers.enums.NoteTypeEnum;
import com.martzatech.vdhg.crmprojectback.domains.security.entities.RoleEntity;
import com.martzatech.vdhg.crmprojectback.domains.security.entities.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
import org.hibernate.annotations.Formula;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "t_notes")
public class NoteEntity implements Serializable {

  @Serial
  private static final long serialVersionUID = 8372381041967502815L;

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "title")
  private String title;

  @Column(name = "description")
  private String description;

  @Column(name = "type")
  @Convert(converter = NoteTypeEnumConverter.class)
  private NoteTypeEnum type;

  @Formula("type")
  private Integer typeAsInt;

  @Column(name = "element_id")
  private Integer elementId;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "t_notes_roles",
      joinColumns = @JoinColumn(name = "note_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id"))
  @With
  private List<RoleEntity> roles;


  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "t_notes_users",
      joinColumns = @JoinColumn(name = "note_id"),
      inverseJoinColumns = @JoinColumn(name = "user_id"))
  @With
  private List<UserEntity> users;

  @ManyToOne
  @JoinColumn(name = "deleted_id")
  private DeletedEntity status;

  @ManyToOne
  @JoinColumn(name = "creation_user_id")
  private UserEntity creationUser;

  @ManyToOne
  @JoinColumn(name = "modification_user_id")
  private UserEntity modificationUser;

  @Column(name = "creation_time")
  private LocalDateTime creationTime;

  @Formula("creation_time")
  private String creationTimeAsStr;

  @Column(name = "modification_time")
  private LocalDateTime modificationTime;
}
