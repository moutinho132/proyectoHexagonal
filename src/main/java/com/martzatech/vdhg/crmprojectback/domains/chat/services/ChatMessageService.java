package com.martzatech.vdhg.crmprojectback.domains.chat.services;

import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.ChatMessageNotFoundException;
import com.martzatech.vdhg.crmprojectback.domains.chat.entities.ChatMessageEntity;
import com.martzatech.vdhg.crmprojectback.domains.chat.mappers.ChatMessageMapper;
import com.martzatech.vdhg.crmprojectback.domains.chat.mappers.ChatRoomMapper;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.ChatMessage;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.ChatRoom;
import com.martzatech.vdhg.crmprojectback.domains.chat.repositories.ChatMessageRepository;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.ChatMessageSpecification;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Slf4j
@Service
public class ChatMessageService {

    private ChatMessageRepository repository;
    private ChatMessageMapper mapper;
    private ChatRoomMapper chatRoomMapper;

    public ChatMessage save(final ChatMessage model) {
        return mapper.entityToModel(repository.save(mapper.modelToEntity(model)));
    }

    public List<ChatMessage> findAllMessageChatRoom(final ChatRoom model){
        return mapper.entitiesToModelList(repository.findAllByChatRoom(chatRoomMapper.modelToEntity(model)));
    }

    public long countMessages(final Integer chatId, final ChatMessageSpecification specification) {
        return repository.count(addChatRoom(chatId, specification));
    }

  public long countChatMessages( final ChatMessageSpecification specification) {
    return repository.count(specification);
  }

    public List<ChatMessage> findAll(final Integer chatId, final ChatMessageSpecification specification,
                                     final Pageable pageable) {
        return mapper.entitiesToModelList(repository.findAll(addChatRoom(chatId, specification), pageable).toList());
    }

    public List<ChatMessage> findAllMessages(final ChatMessageSpecification specification,
                                             final Pageable pageable, final String value,
                                             final String advanceSearch) {
        return mapper.entitiesToModelList(StringUtils.isBlank(value) || StringUtils.isEmpty(value) ?
                repository.findAll(specification, pageable).toList() :
                validateSearchMessage(value, pageable, advanceSearch));
    }

    private List<ChatMessageEntity> validateSearchMessage(final String value, final Pageable pageable, final String advanceSearch) {
        List<ChatMessageEntity> chatMessageEntities = new ArrayList<>();
        if (advanceSearch.equals("YES")) {
            chatMessageEntities = searchMessageDynamicAdvance(value, pageable);
        } else if (advanceSearch.equals("NO")) {
            chatMessageEntities = searchMessageDynamicBasic(value, pageable);
        }
        return chatMessageEntities;
    }

    public List<ChatMessageEntity> searchMessageDynamicBasic(String value, Pageable pageable) {
        String[] words = value.split("\\s+");
        Specification<ChatMessageEntity> spec = null;

        for (String word : words) {
            if (spec == null) {
                spec = Specification.where((root, query, builder) ->
                        builder.like(root.get("value"), "%" + word.toUpperCase() + "%"));
            } else {
                spec = spec.and((root, query, builder) ->
                        builder.like(root.get("value"), "%" + word.toUpperCase() + "%"));
            }
        }
        return repository.findAll(spec, pageable).stream().toList();
    }

    public List<ChatMessageEntity> searchMessageDynamicAdvance(String value, Pageable pageable) {
        String[] words = value.split("\\s+");
        Specification<ChatMessageEntity> spec = null;

        for (String word : words) {
            if (spec == null) {
                spec = Specification.where((root, query, builder) ->
                        builder.like(root.get("value"), "%" + word.toUpperCase() + "%"));
            } else {
                spec = spec.or((root, query, builder) ->
                        builder.like(root.get("value"), "%" + word.toUpperCase() + "%"));
            }
        }
        return repository.findAll(spec, pageable).stream().toList();
    }

    private ChatMessageSpecification addChatRoom(final Integer chatId, final ChatMessageSpecification specification) {
        final ChatMessageSpecification byChatRoom = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.<Integer>get("chatRoom").get("id"), chatId);
        return (root, query, criteriaBuilder) ->
                Objects.nonNull(specification)
                        ? criteriaBuilder
                        .and(
                                specification.toPredicate(root, query, criteriaBuilder),
                                byChatRoom.toPredicate(root, query, criteriaBuilder)
                        )
                        : criteriaBuilder
                        .and(
                                byChatRoom.toPredicate(root, query, criteriaBuilder)
                        );
    }

    private ChatMessageSpecification addChatMessageAdvance(final String advanceSearch, final String value,
                                                           final ChatMessageSpecification specification) {
        final ChatMessageSpecification byChatRoom = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.<String>get("%value%"), value);
        return (root, query, criteriaBuilder) ->
                StringUtils.isNotBlank(advanceSearch) && advanceSearch.equals("YES")
                        ? criteriaBuilder
                        .and(
                                specification.toPredicate(root, query, criteriaBuilder),
                                byChatRoom.toPredicate(root, query, criteriaBuilder)
                        )
                        : criteriaBuilder
                        .and(
                                byChatRoom.toPredicate(root, query, criteriaBuilder)
                        );
    }

    public List<ChatMessage> findLast(final Integer chatId, final Integer messageId, final Pageable pageable) {
        final ChatMessage chatMessage = findById(messageId);
        final Specification<ChatMessageEntity> specification = (root, query, criteriaBuilder) ->
                criteriaBuilder
                        .and(
                                criteriaBuilder.equal(root.<Integer>get("chatRoom").get("id"), chatId),
                                criteriaBuilder.lessThanOrEqualTo(root.get("creationTime").as(LocalDateTime.class),
                                        chatMessage.getCreationTime())
                        );
        return mapper.entitiesToModelList(repository.findAll(specification, pageable).toList());
    }

    public List<ChatMessage> findNext(final Integer chatId, final Integer messageId, final Pageable pageable) {
        final ChatMessage chatMessage = findById(messageId);
        final Specification<ChatMessageEntity> specification = (root, query, criteriaBuilder) ->
                criteriaBuilder
                        .and(
                                criteriaBuilder.equal(root.<Integer>get("chatRoom").get("id"), chatId),
                                criteriaBuilder.greaterThanOrEqualTo(root.get("creationTime").as(LocalDateTime.class),
                                        chatMessage.getCreationTime())
                        );
        return mapper.entitiesToModelList(repository.findAll(specification, pageable).toList());
    }

    public List<ChatMessage> findUnread(final Integer chatId, final Integer currentUserId, final Pageable pageable) {
        return mapper.entitiesToModelList(
                repository.findUnreadMessagesByChatRoom(chatId, currentUserId, pageable));
    }

    public ChatMessage findById(final Integer messageId) {
        return mapper.entityToModel(
                repository.findById(messageId).orElseThrow(() -> new ChatMessageNotFoundException(messageId)));
    }

    public ChatMessage findByCreationTime(final LocalDateTime creationTime) {
        return mapper.entityToModel(
                repository.findByCreationTime(creationTime).orElseThrow(() -> new ChatMessageNotFoundException(null)));
    }

    public ChatMessage findLastMessageByChatRoom(final Integer chatId) {
        final LocalDateTime lastMessageDateTime = repository.findLastMessageIdByChatRoom(chatId);
        return Objects.isNull(lastMessageDateTime) ? null : findByCreationTime(lastMessageDateTime);
    }
    @Transactional
    public ChatMessage findLastMessage(){
        final LocalDateTime lastMessageDateTime = repository.findLastMessage();
        return Objects.isNull(lastMessageDateTime) ? null : findByCreationTime(lastMessageDateTime);
    }

    public ChatMessage findNextMessageByChatRoom(final Integer chatId) {
        final Integer lastMessageId = repository.findNextMessageIdByChatRoom(chatId);
        return Objects.isNull(lastMessageId) ? null : findById(lastMessageId);
    }

    public Integer countUnreadMessagesByChatRoom(final Integer chatId, final Integer currentUserId) {
        return repository.countUnreadMessagesByChatRoom(chatId, currentUserId);
    }

    public void existsById(final Integer id) {
        if (!repository.existsById(id)) {
            throw new ChatMessageNotFoundException(id);
        }
    }

    public void deteleById(final Integer id){
        repository.deleteById(id);
    }
}
