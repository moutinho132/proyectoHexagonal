package com.martzatech.vdhg.crmprojectback.domains.customers.entities;

import com.martzatech.vdhg.crmprojectback.domains.chat.entities.FileEntity;
import com.martzatech.vdhg.crmprojectback.domains.security.entities.SubsidiaryEntity;
import com.martzatech.vdhg.crmprojectback.domains.security.entities.UserEntity;
import com.martzatech.vdhg.crmprojectback.domains.vendors.entities.VendorEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Formula;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "t_products")
public class ProductEntity implements Serializable {

  @Serial
  private static final long serialVersionUID = -1195883458875961093L;

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "name")
  private String name;

  @Column(name = "description")
  private String description;

  @Column(name = "marketing")
  private String marketing;

  @Column(name = "default_vat")
  private Boolean defaultVat = true;

  @Column(name = "product_vat")
  private BigDecimal productVat;

  @Column(name = "default_comission")
  private Boolean defaultCommission ;

  @Column(name = "product_commission")
  private BigDecimal productCommission;

  @Column(name = "price_visible")
  private Boolean priceVisible;

  @Column(name = "base_price")
  private BigDecimal basePrice;

  @ManyToOne
  @JoinColumn(name = "subsidiary_id")
  private SubsidiaryEntity subsidiary;

  @ManyToOne
  @JoinColumn(name = "category_id")
  private CategoryEntity category;

  @OneToOne
  @JoinColumn(name = "vendor_id")
  private VendorEntity vendor;

  @ManyToOne
  @JoinColumn(name = "deleted_id")
  @With
  private DeletedEntity status;

  //TODO incluir vendors

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "t_product_subcategories",
      joinColumns = @JoinColumn(name = "product_id"),
      inverseJoinColumns = @JoinColumn(name = "subcategory_id"))
  private List<SubCategoryEntity> subCategories;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
          name = "t_products_files",
          joinColumns = @JoinColumn(name = "product_id"),
          inverseJoinColumns = @JoinColumn(name = "file_id"))
  private List<FileEntity> files;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "t_product_memberships",
      joinColumns = @JoinColumn(name = "product_id"),
      inverseJoinColumns = @JoinColumn(name = "membership_id"))
  private List<MembershipEntity> memberships;


  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
          name = "t_product_locacion",
          joinColumns = @JoinColumn(name = "product_id"),
          inverseJoinColumns = @JoinColumn(name = "product_location_id"))
  private List<ProductLocationEntity> locations;

  @Column(name = "availability_from")
  private LocalDateTime availabilityFrom;

  @Formula("availability_from")
  private String availabilityFromAsStr;

  @Column(name = "availability_to")
  private LocalDateTime availabilityTo;

  @Formula("availability_to")
  private String availabilityToAsStr;

  @Formula("((now()>=availability_from) && (now()<=availability_to))")
  private Boolean active;

  @Column(name = "visibility")
  private Boolean visibility;

  @ManyToOne
  @JoinColumn(name = "creation_user_id")
  private UserEntity creationUser;

  @ManyToOne
  @JoinColumn(name = "modification_user_id")
  private UserEntity modificationUser;

  @Column(name = "creation_time")
  private LocalDateTime creationTime;

  @Column(name = "modification_time")
  private LocalDateTime modificationTime;
}
