package com.martzatech.vdhg.crmprojectback.domains.customers.services;

import com.martzatech.vdhg.crmprojectback.application.enums.DeletedStatusEnum;
import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.GroupAccountNotFoundException;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.ChatRoom;
import com.martzatech.vdhg.crmprojectback.domains.chat.services.ChatRoomService;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.GroupAccountEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.mappers.GroupAccountMapper;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Customer;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.GroupAccount;
import com.martzatech.vdhg.crmprojectback.domains.customers.repositories.GroupAccountRepository;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.GroupAccountSpecification;
import java.util.List;
import java.util.Objects;

import com.martzatech.vdhg.crmprojectback.domains.security.models.DeletedStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.martzatech.vdhg.crmprojectback.application.helper.SpecificationHelper.addSoftDeleteGroupAccount;

@AllArgsConstructor
@Slf4j
@Service
public class GroupAccountService {

  private GroupAccountRepository repository;
  private GroupAccountMapper mapper;
  private ChatRoomService roomService;
  public GroupAccount save(final GroupAccount model) {
    if (Objects.requireNonNullElse(model.getId(), 0) != 0) {
      existsById(model.getId());
    }
    model.withStatus(DeletedStatus.builder().id(DeletedStatusEnum.ACTIVE.getId()).build());

    return mapper.entityToModel(repository.save(mapper.modelToEntity(model)));
  }

  public List<GroupAccount> findAll(final GroupAccountSpecification specification, final Pageable pageable) {
    return mapper.entitiesToModelList(repository
            .findAll(addSoftDeleteGroupAccount(DeletedStatusEnum.ACTIVE.getId(),specification), pageable)
            .stream()
            .toList());
  }

  public Long count(final GroupAccountSpecification specification) {
    return repository.count(specification);
  }

  public GroupAccount findById(final Integer id) {
   /* GroupAccount account = mapper.entityToModel(repository
            .findById(id).orElseThrow(() -> new GroupAccountNotFoundException(id)));
    ChatRoom  room = null;*/
    /*if(Objects.nonNull(account)){
       room =  roomService.findByIdGroupAccount(account.getId());
     if (Objects.nonNull(room)){
       return  GroupAccount.builder().id(account.getId()).name(account.getName())
               .alias(account.getAlias()).owner(account.getOwner()).chatRoom(room)
               .associates(account.getAssociates())
               .status(account.getStatus())
               .creationUser(account.getCreationUser())
               .creationTime(account.getCreationTime()).build();
     }
    }*/
    return  mapper.entityToModel(repository
            .findById(id).orElseThrow(() -> new GroupAccountNotFoundException(id)));
  }

  public GroupAccount findByCustomer(final Customer customer) {
    return repository.findByCustomerId(customer.getId()).map(mapper::entityToModel).orElse(null);
  }

  public void deleteById(final Integer id) {
    existsById(id);

    repository.deleteById(id);
  }

  public void deleteStatus(final Integer id) {
    existsById(id);

    repository.deleteStatus(id);
  }

  public void existsById(final Integer id) {
    if (!repository.existsById(id)) {
      throw new GroupAccountNotFoundException(id);
    }
  }

  public boolean existsGroupAccountById(final Integer id){
    return repository.existsById(id);
  }

  public GroupAccount findByEmail(final GroupAccount model) {
    return mapper.entityToModel(repository.findByEmail(model.getEmail()));
  }

  public GroupAccount findByName(final GroupAccount model) {
    return mapper.entityToModel(repository.findByName(model.getName()));
  }
}
