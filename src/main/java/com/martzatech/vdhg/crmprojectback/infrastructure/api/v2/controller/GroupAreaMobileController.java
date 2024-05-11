package com.martzatech.vdhg.crmprojectback.infrastructure.api.v2.controller;

import com.martzatech.vdhg.crmprojectback.application.enums.UserTypeEnum;
import com.martzatech.vdhg.crmprojectback.application.services.mobil.SecurityManagementAppService;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Associated;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Customer;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.AssociatedService;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.CustomerAppService;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.GroupAccountService;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import com.martzatech.vdhg.crmprojectback.infrastructure.api.v2.GroupAreaMobileApi;
import com.martzatech.vdhg.crmprojectback.infrastructure.api.v2.response.GroupAccountMobileResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.GroupAccountApiMapper;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v2/group-account-mobile")
@Validated
public class GroupAreaMobileController implements GroupAreaMobileApi {
    private GroupAccountService service;
    private GroupAccountApiMapper mapper;
    private SecurityManagementAppService securityManagementAppService;
    private CustomerAppService customerAppService;
    private AssociatedService associatedService;
    @Override
    public GroupAccountMobileResponse findById( final String token) {
        User user = null;
        user = getUser(token, user);
        Associated associated;
        Customer customer;
        if (user.getTypeUser().equals(UserTypeEnum.CUSTOMER.name())) {
            customer = customerAppService.findById(user.getCustomer().getId(), token);
            return mapper.modelToMobileResponse(service.findById(customer.getGroupAccount().getId()));

        }else{
            associated = associatedService.findById(user.getAssociated().getId());
            return mapper.modelToMobileResponse(service.findById(associated.getGroupAccount().getId()));
        }
    }

    private User getUser(String token, User user) {
        try {
            if(securityManagementAppService.validateToken(token)){
                user = securityManagementAppService.findCurrentUser(token);
            }
        }catch (SecurityException e){
            throw new SecurityException("Error al procesar el token JWT", e);
        }
        return user;
    }

}
