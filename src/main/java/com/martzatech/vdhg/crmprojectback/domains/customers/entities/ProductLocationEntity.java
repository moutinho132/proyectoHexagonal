package com.martzatech.vdhg.crmprojectback.domains.customers.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "t_product_locations")
public class ProductLocationEntity implements Serializable {

  @Serial
  private static final long serialVersionUID = -1195883458875961093L;

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "address")
  private String address;
  @Column(name = "map_url")
  private String mapUrl;

  @Column(name = "latitude")
  private BigDecimal latitude;

  @Column(name = "longitude")
  private BigDecimal longitude;

  @Column(name = "place_id")
  private String placeId;
}
