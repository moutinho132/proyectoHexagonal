package com.martzatech.vdhg.crmprojectback.domains.chat.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.martzatech.vdhg.crmprojectback.domains.commons.entities.DayOfficeEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.DeletedEntity;
import com.martzatech.vdhg.crmprojectback.domains.security.entities.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "t_chat_out_office")
public class ChatOutOfficeEntity implements Serializable {

  @Serial
  private static final long serialVersionUID = 6034881482730312839L;

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "value")
  private String value;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
          name = "t_chat_out_office_day",
          joinColumns = @JoinColumn(name = "out_office_id"),
          inverseJoinColumns = @JoinColumn(name = "out_office_day_id"))
  @With
  private List<DayOfficeEntity> days;
  @JsonFormat(timezone = "UTC", pattern = "yyyy-MM-dd'T'HH:mm:SSSSSSSSS")
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @With
  @Column(name = "creation_time_start")
  private String start;
  @JsonFormat(timezone = "UTC", pattern = "yyyy-MM-dd HH:mm:ss.SSSSSSSSS")
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @With
  @Column(name = "creation_time_end")
  private String end;

  @With
  @Column(name = "name")
  private String name;

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
