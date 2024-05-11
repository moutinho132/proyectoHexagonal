package com.martzatech.vdhg.crmprojectback.domains.security.entities;

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
@Table(name = "t_users_mobil")
public class UserMobilEntity implements Serializable {

  @Serial
  private static final long serialVersionUID = -7853544041236457000L;

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;


  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private UserEntity user;

  @Column(name = "password")
  private String password;


  @Column(name = "last_login")
  private LocalDateTime lastLogin;

  @Column(name = "password_update_requered")
  private boolean passwordUpdateRequired; // Nuevo campo para indicar si se debe actualizar la contraseña
}
