package com.martzatech.vdhg.crmprojectback.domains.chat.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.martzatech.vdhg.crmprojectback.domains.chat.converters.ChatMessageTypeEnumConverter;
import com.martzatech.vdhg.crmprojectback.domains.chat.enums.ChatMessageTypeEnum;
import com.martzatech.vdhg.crmprojectback.domains.security.entities.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "t_chat_messages")
public class ChatMessageEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoomEntity chatRoom;

    @Column(name = "type")
    @Convert(converter = ChatMessageTypeEnumConverter.class)
    private ChatMessageTypeEnum type;

    @Column(name = "value")
    private String value;

    @OneToMany(mappedBy = "message", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessageReaderEntity> readers;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "t_chat_files",
            joinColumns = @JoinColumn(name = "message_id"),
            inverseJoinColumns = @JoinColumn(name = "file_id"))
    @With
    private List<FileEntity> files;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private UserEntity sender;

    @JsonFormat(timezone = "UTC", pattern = "yyyy-MM-dd HH:mm:ss.SSSSSSSSS")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Column(name = "creation_time")
    private LocalDateTime creationTime;
}
