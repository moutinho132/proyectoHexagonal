package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.controllers;

import com.martzatech.vdhg.crmprojectback.application.services.AssociatedManagementService;
import com.martzatech.vdhg.crmprojectback.application.services.GroupAccountManagementService;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.ChatRoom;
import com.martzatech.vdhg.crmprojectback.domains.chat.services.ChatRoomService;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.GroupAccount;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.GroupAccountService;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.GroupAccountSpecification;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.GroupAccountApi;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.AssociatedApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.GroupAccountApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.AssociatedRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.GroupAccountRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.AssociatedResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.GroupAccountResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.PaginatedResponse;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@RestController
@RequestMapping("/api/group-accounts")
@Validated
public class GroupAccountController implements GroupAccountApi {

  private GroupAccountService service;
  private GroupAccountManagementService managementService;
  private GroupAccountApiMapper mapper;
  private AssociatedManagementService associatedManagementService;
  private AssociatedApiMapper associatedApiMapper;
  private ChatRoomService roomService;

  @Override
  public GroupAccountResponse save(final GroupAccountRequest request) {
    return mapper.modelToResponse(managementService.save(mapper.requestToModel(request)));
  }

  @Override
  public PaginatedResponse<GroupAccountResponse> findAll(final GroupAccountSpecification specification,
      final Integer page, final Integer size, final String direction, final String attribute) {
    final Long total = service.count(specification);
    final List<GroupAccount> response = managementService.findAll(specification, page, size, direction, attribute);
    return PaginatedResponse.<GroupAccountResponse>builder()
        .total(total.intValue())
        .page(page)
        .size(response.size())
        .items(mapper.modelsToResponseList(response))
        .build();
  }

  @Override
  public GroupAccountResponse findById(final Integer id) {
    GroupAccount account = service.findById(id);
    ChatRoom room =  roomService.findByIdGroupAccount(account);
    return mapper.modelToResponse(service.findById(id).withChatRoom(Objects.nonNull(room)?room:null));
  }

  @Override
  public void deleteById(final Integer id) {
    service.deleteById(id);
  }
  @Override
  public void deleteStatus(final Integer id) {
    service.deleteStatus(id);
  }
  @Override
  public AssociatedResponse saveAssociated(final Integer groupAccountId, final AssociatedRequest request) {
    return associatedApiMapper.modelToResponse(
        associatedManagementService.save(groupAccountId, associatedApiMapper.requestToModel(request))
    );
  }

  @Override
  public void deleteAssociatedById(final Integer groupAccountId, final Integer associatedId) {
    associatedManagementService.deleteById(groupAccountId, associatedId);
  }
}
