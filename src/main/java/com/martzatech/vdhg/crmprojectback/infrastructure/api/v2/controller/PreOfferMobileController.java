package com.martzatech.vdhg.crmprojectback.infrastructure.api.v2.controller;

import com.martzatech.vdhg.crmprojectback.application.services.mobil.PreOfferManagementAppService;
import com.martzatech.vdhg.crmprojectback.application.services.mobil.SecurityManagementAppService;
import com.martzatech.vdhg.crmprojectback.domains.chat.services.ChatMessageService;
import com.martzatech.vdhg.crmprojectback.domains.chat.services.ChatRoomService;
import com.martzatech.vdhg.crmprojectback.domains.chat.services.FileService;
import com.martzatech.vdhg.crmprojectback.domains.customers.enums.OfferStatusEnum;
import com.martzatech.vdhg.crmprojectback.domains.customers.enums.PreOfferStatusEnum;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.PreOffer;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.AssociatedService;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.CustomerAppService;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.GroupAccountService;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.mobil.PreOfferAppService;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.PreOfferV2MobilSpecification;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import com.martzatech.vdhg.crmprojectback.infrastructure.api.v2.PreOfferMobileApi;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.FileApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.PreOfferApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.PreOfferProductApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.PaginatedResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.PreOfferResponse;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v2/pre-offers-mobile")
@Validated
public class PreOfferMobileController implements PreOfferMobileApi {

    private PreOfferAppService service;
    private PreOfferManagementAppService managementService;
    private PreOfferApiMapper mapper;
    private PreOfferProductApiMapper preOfferProductApiMapper;
    private FileApiMapper fileApiMapper;
    private SecurityManagementAppService securityManagementAppService;
    private SecurityManagementAppService securityManagementService;
    private CustomerAppService customerAppService;
    private AssociatedService associatedService;
    private ChatRoomService chatRoomService;
    private GroupAccountService groupAccountService;
    private ChatMessageService messageService;
    private FileService fileService;

    @Override
    public PaginatedResponse<PreOfferResponse> findAll(PreOfferV2MobilSpecification specification, final Integer page,
                                                       final Integer size, final String direction, final String attribute, final String token) {
        User user = null;
        if (securityManagementAppService.validateToken(token)) {
            user = securityManagementAppService.findCurrentUser(token);
            specification = new PreOfferV2MobilSpecification(PreOfferStatusEnum.WORKING, user.getCustomer(), OfferStatusEnum.SENT);
        }

        final Long total = service.count(specification);
        final List<PreOffer> response = managementService.findAll(specification, page, size, direction, attribute);
        return PaginatedResponse.<PreOfferResponse>builder()
                .total(total.intValue())
                .page(page)
                .size(response.size())
                .items(mapper.modelsToResponseList(response))
                .build();
    }

    @Override
    public boolean sendMobileChat(final Integer id, final String token) {
        return  managementService.sendMobileChatService(id,token);
    }

}
