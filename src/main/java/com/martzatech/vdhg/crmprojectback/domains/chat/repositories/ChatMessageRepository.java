package com.martzatech.vdhg.crmprojectback.domains.chat.repositories;

import com.martzatech.vdhg.crmprojectback.domains.chat.entities.ChatMessageEntity;
import com.martzatech.vdhg.crmprojectback.domains.chat.entities.ChatRoomEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Integer>,
    JpaSpecificationExecutor<ChatMessageEntity> {
  @Query("select e from ChatMessageEntity e where e.id = :id")
  Optional<ChatMessageEntity> findById(Integer id);


  List<ChatMessageEntity> findAllByChatRoom(ChatRoomEntity entity);
  Optional<ChatMessageEntity> findByCreationTime(LocalDateTime creationTime);

  @Query(value = "SELECT max(tcm.creation_time)\n" +
          "FROM vdhg_db.t_chat_rooms tcr inner join vdhg_db.t_chat_messages tcm on tcm.chat_room_id =tcr.id \n" +
          "where tcr.id =:chatId"
      , nativeQuery = true)
  LocalDateTime findLastMessageIdByChatRoom(@Param("chatId") Integer chatId);

  @Query(value = "select max(m.creation_time) from t_chat_messages m", nativeQuery = true)
  LocalDateTime findLastMessage();

  @Query(value = "select min(m.id) from t_chat_messages m "
          + "       join t_chat_rooms cr on cr.id = m.chat_room_id"
          + "      where cr.id = :chatId"
          , nativeQuery = true)
  Integer findNextMessageIdByChatRoom(@Param("chatId") Integer chatId);

  @Query(value = "select m from ChatMessageEntity m "
          + "where m.chatRoom.id = :chatId "
          + "  and m.sender.id != :currentUserId "//TODO:ALEXIA
          + "  and :currentUserId not in (select mr.reader.id from ChatMessageReaderEntity mr where mr.message.id = m.id) "
          + "  and ( m.chatRoom.type = 1 or (m.chatRoom.type = 2 or m.chatRoom.type = 3"
          + "    and 'CHATS_VIEW_ALL' not in ( "
          + "    select pe.name from UserEntity ue "
          + "      join RoleEntity re "
          + "      join PermissionEntity pe "
          + "     where ue.id in (select mr.reader.id from ChatMessageReaderEntity mr where mr.message.id = m.id) "
          + "        ) "
          + "      )) ")
  List<ChatMessageEntity> findUnreadMessagesByChatRoom(@Param("chatId") Integer chatId,
      @Param("currentUserId") Integer currentUserId, Pageable pageable);

  @Query(value = "select count(m.id) "
      + " from t_chat_messages m "
      + " join t_chat_rooms cr on cr.id = m.chat_room_id "
      + "where cr.id = :chatId "
      + "  and m.sender_id != :currentUserId "
      + "  and :currentUserId not in (select mr.reader_id from t_chat_message_readers mr where  mr.message_id = m.id) "
      + "  and ( cr.`type` = 1 or ( cr.`type` = 2 or cr.`type` = 3"
      + "    and 'CHATS_VIEW_ALL' not in ( "
      + "      select tp.name from t_permissions tp "
      + "        join t_roles_permissions trp on trp.permission_id = tp.id "
      + "        join t_roles tr on tr.id = trp.role_id "
      + "        join t_users_roles tur on tur.role_id = tr.id "
      + "       where tur.user_id in (select mr.reader_id from t_chat_message_readers mr where mr.message_id = m.id) "
      + "    ))"
      + "  )", nativeQuery = true)
  Integer countUnreadMessagesByChatRoom(@Param("chatId") Integer chatId, @Param("currentUserId") Integer currentUserId);
}
