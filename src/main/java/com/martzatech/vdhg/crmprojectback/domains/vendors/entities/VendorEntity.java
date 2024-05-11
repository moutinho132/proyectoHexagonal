package com.martzatech.vdhg.crmprojectback.domains.vendors.entities;

import com.martzatech.vdhg.crmprojectback.domains.security.entities.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "t_vendors")
public class VendorEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -1195883458875961094L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "billing_address")
    private String billingAddress;

    @Column(name = "vat")
    private BigDecimal vat;

    @Column(name = "company_reg")
    private String companyReg;

    @Column(name = "finance_email")
    private String financeEmail;

    @Column(name = "payment_detail")
    private String paymentDetails;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "commission")
    private BigDecimal commission;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.MERGE)
    @JoinTable(
            name = "t_vendor_locations",
            joinColumns = @JoinColumn(name = "vendor_id"),
            inverseJoinColumns = @JoinColumn(name = "locations_id"))
    private List<VendorLocationEntity> vendorLocations;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.MERGE)
    @JoinTable(
            name = "t_vendor_contacts",
            joinColumns = @JoinColumn(name = "vendor_id"),
            inverseJoinColumns = @JoinColumn(name = "contact_id"))
    private List<VendorContactEntity> vendorContacts;

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
