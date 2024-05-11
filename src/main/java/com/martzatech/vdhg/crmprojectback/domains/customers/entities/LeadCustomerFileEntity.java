package com.martzatech.vdhg.crmprojectback.domains.customers.entities;


import com.martzatech.vdhg.crmprojectback.domains.commons.entities.PersonEntity;
import com.martzatech.vdhg.crmprojectback.domains.security.entities.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "t_lead_customer_file")
public class LeadCustomerFileEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -8536297222423983697L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "url")
    private String url;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "lead_id")
    private LeadEntity lead;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private PersonEntity person;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private CustomerEntity customer;

    @ManyToOne
    @JoinColumn(name = "removal_user_id")
    private UserEntity removalUser;

    @ManyToOne
    @JoinColumn(name = "creation_user_id")
    private UserEntity creationUser;

    @ManyToOne
    @JoinColumn(name = "deleted_id")
    private DeletedEntity status;

    @Column(name = "creation_time")
    private LocalDateTime creationTime;

    @Column(name = "removal_time")
    private LocalDateTime removalTime;
}
