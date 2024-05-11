package com.martzatech.vdhg.crmprojectback.domains.chat.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.martzatech.vdhg.crmprojectback.domains.chat.converters.ChatRoomTypeEnumConverter;
import com.martzatech.vdhg.crmprojectback.domains.chat.enums.ChatRoomTypeEnum;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.CustomerEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.GroupAccountEntity;
import com.martzatech.vdhg.crmprojectback.domains.security.entities.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "t_chat_rooms")
public class ChatRoomEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private CustomerEntity customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_account_id")
    private GroupAccountEntity groupAccount;

    @Column(name = "type")
    @Convert(converter = ChatRoomTypeEnumConverter.class)
    private ChatRoomTypeEnum type;

    @Column(name = "archive")
    private boolean archive = Boolean.FALSE;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "t_chat_room_members",
            joinColumns = @JoinColumn(name = "chat_room_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id"))
    private List<UserEntity> members;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creation_user_id")
    private UserEntity creationUser;
    @JsonFormat(timezone = "UTC", pattern = "yyyy-MM-dd HH:mm:ss.SSSSSSSSS")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Column(name = "creation_time")
    private LocalDateTime creationTime;
}
