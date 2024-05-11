package com.martzatech.vdhg.crmprojectback.domains.security.entities;

import com.martzatech.vdhg.crmprojectback.domains.chat.entities.ChatRoomEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.AssociatedEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.CustomerEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Formula;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "t_users")
public class UserEntity implements Serializable {

  @Serial
  private static final long serialVersionUID = -7853544041236457000L;

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "title", nullable = false, length = 4)
  private String title;

  @Column(name = "name", nullable = false, length = 128)
  private String name;

  @Column(name = "surname", nullable = false, length = 128)
  private String surname;

  @Column(name = "email", nullable = false, length = 128, unique = true)
  private String email;

  @Formula("concat_ws(' ',name,surname,email)")
  private String fullName;

  @Column(name = "mobile", nullable = false, length = 32)
  private String mobile;

  @Column(name = "address", length = 512)
  private String address;

  @Column(name = "nationality", length = 4)
  private String nationality;

  @Column(name = "type_user", length = 100)
  private String typeUser;

  @ManyToOne
  @JoinColumn(name = "status_id")
  @With
  private UserStatusEntity status;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "associated_id")
  private AssociatedEntity associated;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "customer_id")
  private CustomerEntity customer;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "t_users_roles",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id"))
  @With
  private List<RoleEntity> roles;

  @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.REMOVE)
  @JoinTable(
          name = "t_chat_room_members",
          joinColumns = @JoinColumn(name = "member_id"),
          inverseJoinColumns = @JoinColumn(name = "chat_room_id"))
  @With
  private List<ChatRoomEntity> chatRoom;

/*
  @OneToMany(mappedBy = "user", fetch = FetchType.EAGER,cascade = CascadeType.ALL,orphanRemoval = true)
  private List<UserMobilEntity> userMobil;
*/

  @Column(name = "creation_time")
  private LocalDateTime creationTime;

  @Column(name = "modification_time")
  private LocalDateTime modificationTime;

  @Column(name = "last_login")
  private LocalDateTime lastLogin;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "creation_user_id")
  private UserEntity creationUser;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "modification_user_id")
  private UserEntity modificationUser;
}
