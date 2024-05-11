package com.martzatech.vdhg.crmprojectback.domains.chat.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.martzatech.vdhg.crmprojectback.domains.commons.entities.PersonEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.*;
import com.martzatech.vdhg.crmprojectback.domains.security.entities.UserEntity;
import com.martzatech.vdhg.crmprojectback.domains.vendors.entities.VendorEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "t_files")
public class FileEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "url")
    private String url;

    @Column(name = "name")
    private String name;

    @Column(name = "extension")
    private String extension;

    @Column(name = "text")
    private String text;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "t_person_file",
            joinColumns = @JoinColumn(name = "file_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id"))
    @With
    private List<PersonEntity> persons;



    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "t_products_files",
            joinColumns = @JoinColumn(name = "file_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    @With
    private List<ProductEntity> products;

    /*@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "t_bookings_offer_files",
            joinColumns = @JoinColumn(name = "file_id"),
            inverseJoinColumns = @JoinColumn(name = "offer_id"))
    @With
    private List<OfferEntity> offers;*/

   /* @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "t_bookings_pre_offer_files",
            joinColumns = @JoinColumn(name = "file_id"),
            inverseJoinColumns = @JoinColumn(name = "pre_offer_id"))
    @With
    private List<PreOfferEntity> preOffers;*/

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "t_bookings_products_files",
            joinColumns = @JoinColumn(name = "file_id"),
            inverseJoinColumns = @JoinColumn(name = "bocking_product_id"))
    @With
    private List<BockingProductEntity> bockingProduct;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "t_vendor_files",
            joinColumns = @JoinColumn(name = "file_id"),
            inverseJoinColumns = @JoinColumn(name = "vendor_id"))
    @With
    private List<VendorEntity> vendors;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "t_chat_files",
            joinColumns = @JoinColumn(name = "file_id"),
            inverseJoinColumns = @JoinColumn(name = "message_id"))
    @With
    private List<ChatMessageEntity> messages;

    /*@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "t_order_file",
            joinColumns = @JoinColumn(name = "file_id"),
            inverseJoinColumns = @JoinColumn(name = "order_id"))
    @With
    private List<OrderEntity> orders;*/


    @Column(name = "removal_time")
    private LocalDateTime removalTime;

    @JsonFormat(timezone = "UTC", pattern = "yyyy-MM-dd'T'HH:mm:SSSSSSSSS")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Column(name = "creation_time")
    private LocalDateTime creationTime;

    @ManyToOne
    @JoinColumn(name = "removal_user_id")
    private UserEntity removalUser;

    @ManyToOne
    @JoinColumn(name = "deleted_id")
    private DeletedEntity status;

    @ManyToOne
    @JoinColumn(name = "creation_user_id")
    private UserEntity creationUser;
}
