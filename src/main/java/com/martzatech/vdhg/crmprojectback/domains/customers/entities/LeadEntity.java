package com.martzatech.vdhg.crmprojectback.domains.customers.entities;

import com.martzatech.vdhg.crmprojectback.domains.commons.entities.PersonEntity;
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
@Table(name = "t_lead")
public class LeadEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -1195883458875961093L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "registration_type_id")
    private RegistrationTypeEntity registrationType;

    @OneToOne
    @JoinColumn(name = "person_id")
    private PersonEntity person;

    @ManyToOne
    @JoinColumn(name = "referring_id")
    private CustomerEntity referringCustomer;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private LeadStatusEntity status;

    @ManyToOne
    @JoinColumn(name = "deleted_id")
    @With
    private DeletedEntity deletedStatus;

    @Column(name = "reference")
    private String reference;

    @Column(name = "creation_time")
    private LocalDateTime creationTime;

    @Column(name = "modification_time")
    private LocalDateTime modificationTime;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private CompanyEntity company;
}
