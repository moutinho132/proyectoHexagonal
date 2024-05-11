package com.martzatech.vdhg.crmprojectback.infrastructure.api.v2.controller;

import com.martzatech.vdhg.crmprojectback.application.enums.UserTypeEnum;
import com.martzatech.vdhg.crmprojectback.application.services.mobil.OfferManagementAppService;
import com.martzatech.vdhg.crmprojectback.application.services.mobil.SecurityManagementAppService;
import com.martzatech.vdhg.crmprojectback.domains.chat.enums.ChatMessageTypeEnum;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.ChatMessage;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.ChatRoom;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.File;
import com.martzatech.vdhg.crmprojectback.domains.chat.services.ChatMessageService;
import com.martzatech.vdhg.crmprojectback.domains.chat.services.ChatRoomService;
import com.martzatech.vdhg.crmprojectback.domains.chat.services.FileService;
import com.martzatech.vdhg.crmprojectback.domains.customers.enums.OfferGLobalStatusEnum;
import com.martzatech.vdhg.crmprojectback.domains.customers.enums.OfferStatusEnum;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Offer;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.AssociatedService;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.CustomerAppService;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.GroupAccountService;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.mobil.OfferMobileService;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.OfferV2MobilSpecification;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import com.martzatech.vdhg.crmprojectback.infrastructure.api.v2.OfferMobileApi;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.FileApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.OfferApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.PreOfferProductApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.OfferMobileResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.PaginatedResponse;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v2/offers-mobile")
@Validated
public class OfferMobileController implements OfferMobileApi {

  private OfferMobileService service;
  private OfferManagementAppService managementService;
  private OfferApiMapper mapper;
  private FileService fileService;
  private PreOfferProductApiMapper preOfferProductApiMapper;
  private FileApiMapper fileApiMapper;
  private SecurityManagementAppService securityManagementService;
  private CustomerAppService customerAppService;
  private AssociatedService associatedService;
  private ChatRoomService chatRoomService;
  private GroupAccountService groupAccountService;
  private ChatMessageService messageService;
  @Override
  public PaginatedResponse<OfferMobileResponse> findAll(OfferV2MobilSpecification specification, final Integer page,
                                                        final Integer size, final String direction, final String attribute,
                                                        final Boolean active, String name, BigDecimal priceFrom, BigDecimal priceTo,
                                                        final Boolean restricted,
                                                        final String token) {
    final Long total = service.count(specification);

    User user = null;
    if (securityManagementService.validateToken(token)) {
      user = securityManagementService.findCurrentUser(token);
    }
    if (Objects.nonNull(user) && user.getTypeUser()== UserTypeEnum.CUSTOMER.name()) {
      specification = new OfferV2MobilSpecification(OfferGLobalStatusEnum.WORKING, user.getCustomer(),
              OfferStatusEnum.SENT,active,name,priceFrom,priceTo,null);
    }else{
      specification = new OfferV2MobilSpecification(OfferGLobalStatusEnum.WORKING, user.getCustomer(),
              OfferStatusEnum.SENT,active,name,priceFrom,priceTo,false);
    }

    final List<Offer> response = managementService.findAll(specification, page, size, direction,
            attribute);

    return PaginatedResponse.<OfferMobileResponse>builder()
        .total(total.intValue())
        .page(page)
        .size(response.size())
        .items(mapper.modelsToResponseMobileList(response))
        .build();
  }

  public List<OfferMobileResponse> findByNumber(final Integer number,final String token) {
    return mapper.modelsToResponseMobileList(service.findByNumber(number,token)
            .stream()
            .collect(Collectors.toList()));
  }

  @Override
  public boolean sendMobileChat(final Integer id,final String token) {
    return managementService.sendMobileChatService(id,token);

  }


  private boolean buildMessageCustomerChat(ChatRoom chatRoom, Offer offer,User sender,File file) {
    if (Objects.nonNull(chatRoom)) {
      messageService.save(ChatMessage.builder().chatRoom(chatRoom)
              .value(" I would like to receive more information about this proposal " + offer.getName())
              .sender(sender)
              .type(ChatMessageTypeEnum.FILE)
              .files(List.of(fileApiMapper.modelToResponse(file)))
              .build());
      return true;
    }
    return false;
  }
}
