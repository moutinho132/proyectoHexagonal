package com.martzatech.vdhg.crmprojectback.domains.chat.repositories;

import com.martzatech.vdhg.crmprojectback.domains.chat.entities.ChatRoomEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.GroupAccountEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, Integer>,
    JpaSpecificationExecutor<ChatRoomEntity> {

  @Modifying
  @Transactional
  @Query(value = "DELETE from ChatRoomEntity c where c.id = :id")
  void deleteById(@Param("id") Integer id);

  Optional<ChatRoomEntity> findByGroupAccount(GroupAccountEntity account);
  @Query(value = "SELECT chr.*\n" +
          "FROM vdhg_db.t_chat_rooms chr \n" +
          "inner join t_chat_room_members tcrm on chr.id =tcrm.chat_room_id \n" +
          "inner join t_users tu on tu.id= tcrm.member_id\n" +
          "where tcrm.member_id = :id and chr.`type` =2",nativeQuery = true)
  List<ChatRoomEntity> findByIdMember(@Param("id") Integer id);

  @Query(value = "select count(*) from t_chat_rooms tcr " +
          "inner join t_chat_room_members tcrm " +
          "on tcrm.chat_room_id =tcr.id where tcrm.member_id =:id and tcr.`type` =1 ",nativeQuery = true)
  Long findByIdMemberChat(@Param("id") Integer id);

  @Query("select e from ChatRoomEntity e where e.customer.id = :id and e.type=2")
  ChatRoomEntity findByIdCustomer(@Param("id") Integer id);

  @Modifying
  @Query(value = "update ChatRoomEntity c set c.archive=true where c.id = :id")
  @Transactional
  void updateChatArchive(@Param("id") Integer id);

  @Query(value = "select count(ch.id)from ChatRoomEntity ch where ch.type=3 and ch.customer.id=:id")
  Long findByIdMemberGroupAccount(@Param("id") Integer id);
}
