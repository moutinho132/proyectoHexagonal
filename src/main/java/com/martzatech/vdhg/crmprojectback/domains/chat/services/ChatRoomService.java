package com.martzatech.vdhg.crmprojectback.domains.chat.services;

import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.ChatRoomNotFoundException;
import com.martzatech.vdhg.crmprojectback.domains.chat.entities.ChatRoomEntity;
import com.martzatech.vdhg.crmprojectback.domains.chat.enums.ChatRoomTypeEnum;
import com.martzatech.vdhg.crmprojectback.domains.chat.mappers.ChatRoomMapper;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.ChatRoom;
import com.martzatech.vdhg.crmprojectback.domains.chat.repositories.ChatRoomRepository;
import com.martzatech.vdhg.crmprojectback.domains.customers.mappers.GroupAccountMapper;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.GroupAccount;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.ChatRoomTypeSpecification;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.ChatRoomv1Specification;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.ChatRoomv2Specification;
import com.martzatech.vdhg.crmprojectback.domains.security.services.UserService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@AllArgsConstructor
@Slf4j
@Service
public class ChatRoomService {
    private static final String INTERNAL_PERMISSION = "CHATS_VIEW_ALL";

    private ChatRoomRepository repository;
    private ChatRoomMapper mapper;
    private UserService userService;
    private GroupAccountMapper accountMapper;

    public ChatRoom save(final ChatRoom model) {
        return mapper.entityToModel(repository.save(mapper.modelToEntity(model)));
    }

    public List<ChatRoom> findAll(final ChatRoomv1Specification specification, final Integer currentUserId,
                                  final Boolean isInternalUser, final Pageable pageable) {

        return mapper.entitiesToModelList(repository.findAll(specification, pageable).stream()
                .toList());
    }

    public List<ChatRoom> findAllChatArchive(final ChatRoomv2Specification specification, final Integer currentUserId,
                                  final Boolean isInternalUser, final Pageable pageable) {

        return mapper.entitiesToModelList(repository.findAll(specification, pageable).stream()
                .toList());
    }
    public void deleteChat(final Integer id){
        existsById(id);
        repository.deleteById(id);//Sobreescribir
    }
    public List<ChatRoom> findAllTypeAndPriority(final ChatRoomv1Specification specification, final Integer currentUserId,
                                                 final Boolean isInternalUser, final Pageable pageable) {
        return mapper.entitiesToModelList(repository.findAll(specification, pageable).stream()
                .toList());
    }

    public static Specification<ChatRoomEntity> getByType(ChatRoomTypeEnum typeCustomer) {
        return (Root<ChatRoomEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.equal(root.get("type"), typeCustomer);

            return predicate;
        };
    }
    @Transactional
    public ChatRoom findByIdCustomer(final Integer idCustomer) {
        return mapper.entityToModel(repository.findByIdCustomer(idCustomer));
    }

    public List<ChatRoom> findAllChatRoomCustomer(final Integer idCustomer) {
        return mapper.entitiesToModelList(repository.findByIdMember(idCustomer));
    }

    public long findAllChatRoomCustomerChat(final Integer id) {
        return repository.findByIdMemberChat(id);
    }


    public Long findAllChatRoomAssociate(final Integer idCustomer) {
        return repository.findByIdMemberGroupAccount(idCustomer);
    }
    @Transactional
    public ChatRoom findById(final Integer chatId) {
        return mapper.entityToModel(repository.findById(chatId).orElseThrow(() -> new ChatRoomNotFoundException(chatId)));
    }


    public void updateArchiveChat(final Integer id) {
        repository.updateChatArchive(id);
    }
    public void existsById(final Integer id) {
        if (!repository.existsById(id)) {
            throw new ChatRoomNotFoundException(id);
        }
    }


    public ChatRoom findByIdGroupAccount(final GroupAccount model) {
        ChatRoom room = null;
        Optional<ChatRoomEntity> chatRoomEntity = repository.findByGroupAccount(accountMapper.modelToEntity(model));
        if(chatRoomEntity.isPresent()){
            room = mapper.entityToModel(chatRoomEntity.get());
        }
        return room;
    }

    public long countChatRoom(ChatRoomv1Specification specification) {
        return repository.count(specification);
    }

    public long countChatRoomArchive(ChatRoomv2Specification specification) {
        return repository.count(specification);
    }

    public long countChatRoomTypeCustomer(ChatRoomTypeSpecification specification) {
        return repository.count(specification);
    }
}
