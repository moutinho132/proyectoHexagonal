package com.martzatech.vdhg.crmprojectback.domains.commons.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "t_addresses")
public class AddressEntity implements Serializable {

  @Serial
  private static final long serialVersionUID = 4454711390594132926L;

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "country_id")
  private CountryEntity country;

  @Column(name = "province")
  private String province;

  @Column(name = "street")
  private String street;

  @Column(name = "complement")
  private String complement;

  @Column(name = "zip_code")
  private String zipCode;

  @Column(name = "city")
  private String city;

  @ManyToOne
  @JoinColumn(name = "attribute_type_id")
  private AttributeTypeEntity attributeType;

  @JsonFormat(timezone = "UTC", pattern = "yyyy-MM-dd'T'HH:mm:SSSSSSSSS")
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @Column(name = "creation_time")
  private LocalDateTime creationTime;

  @Column(name = "modification_time")
  private LocalDateTime modificationTime;

  @ManyToOne
  @JoinColumn(name = "person_id")
  @With
  private PersonEntity person;
}
