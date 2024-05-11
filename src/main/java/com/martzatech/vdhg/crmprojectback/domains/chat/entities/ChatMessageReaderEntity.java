package com.martzatech.vdhg.crmprojectback.domains.chat.entities;

import com.martzatech.vdhg.crmprojectback.domains.security.entities.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "t_chat_message_readers")
public class ChatMessageReaderEntity {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "message_id")
  private ChatMessageEntity message;

  @ManyToOne
  @JoinColumn(name = "reader_id")
  private UserEntity reader;

  @Column(name = "reading_time")
  private LocalDateTime readingTime;
}
